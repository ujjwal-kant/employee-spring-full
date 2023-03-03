package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.data.OrderDetailData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;

public class PdfDtoTest extends AbstractUnitTest{

    @Autowired
    private PdfDto pdfDto;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Before
    public void addProduct() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.addBrandCategory(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName("product1");
        productPojo.setBarcode("barcode1");
        productPojo.setMrp(2.97);
        productPojo.setBrandCategoryId(brandPojo.getId());
        productService.add(productPojo);
        
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.add(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("product2");
        productPojo2.setBarcode("barcode2");
        productPojo2.setMrp(2.98);
        productPojo2.setBrandCategoryId(brandPojo.getId());
        productService.add(productPojo2);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(5);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.add(inventoryPojo2);
    }

    @Test
    public void testHappyGetPdf() throws ApiException, IOException{
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("barcode1");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1.0);

        barcodes.add("barcode2");
        quantities.add(1);
        sellingPrices.add(1.0);

        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        OrderDetailData order = orderDto.addOrder(orderItemFormList);

        pdfDto.get(order.getId());

        OrderPojo orderPojo = orderService.getById(order.getId());

        assertEquals(orderPojo.getIsInvoiceCreated(),true);
    }

    @Test
    public void testSadGetPdfIfOrderContainsNoItems() throws ApiException, IOException{
        OrderPojo order = orderService.createNewOrder();
        try{
            pdfDto.get(order.getId());
        }
        catch(ApiException e){
            assertEquals("No order items present in order to place", e.getMessage());
        }
    }

    @Test
    public void testSadGetPdfIfOrderIdIsWrong() throws ApiException, IOException{
        
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("barcode1");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1.0);

        barcodes.add("barcode2");
        quantities.add(1);
        sellingPrices.add(1.0);

        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        orderDto.addOrder(orderItemFormList);
        Integer id=2;

        try{
            pdfDto.get(id);
        }
        catch(ApiException e){
            assertEquals("Order with given id not found", e.getMessage());
        }
    }

    @Test
    public void testHappyDownloadPdf() throws ApiException, IOException{
        
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("barcode1");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1.0);

        barcodes.add("barcode2");
        quantities.add(1);
        sellingPrices.add(1.0);

        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        OrderDetailData order = orderDto.addOrder(orderItemFormList);

        pdfDto.get(order.getId());

        OrderPojo orderPojo = orderService.getById(order.getId());

        ResponseEntity<byte[]> response = pdfDto.download(order.getId());

        assertNotNull(response);
        
    }

}
