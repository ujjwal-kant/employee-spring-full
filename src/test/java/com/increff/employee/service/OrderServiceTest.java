package com.increff.employee.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.helper.TestUtils;
import com.increff.employee.model.data.OrderDetailData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;

public class OrderServiceTest extends AbstractUnitTest{
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderDto orderDto;

    protected BrandPojo DummyBrandPojo(String Brand,String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(Brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    protected ProductPojo DummyProductPojo(String name,String barcode,Integer brandCategoryId,Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setBrand_category_id(brandCategoryId);
        return productPojo;        
    }

    protected InventoryPojo DummyInventoryPojo(Integer productId,Integer Quantity){
        InventoryPojo InventoryPojo = new InventoryPojo();
        InventoryPojo.setProductId(productId);
        InventoryPojo.setQuantity(Quantity);
        return InventoryPojo;
    }

    @Before
    public void addProduct() throws ApiException{
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.addBrandCategory(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName("product1");
        productPojo.setBarcode("barcode1");
        productPojo.setMrp(2.97);
        productPojo.setBrand_category_id(brandPojo.getId());
        productService.add(productPojo);
        
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.add(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("product2");
        productPojo2.setBarcode("barcode2");
        productPojo2.setMrp(2.98);
        productPojo2.setBrand_category_id(brandPojo.getId());
        productService.add(productPojo2);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(5);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.add(inventoryPojo2);
    }
    

    @Test
    public void testHappyCreateNewOrder() throws ApiException{
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("barcode1");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1.0);

        barcodes.add("barcode2");
        quantities.add(1);
        sellingPrices.add(1.0);

        OrderPojo o = orderService.createNewOrder();

        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        OrderDetailData order = orderDto.addOrder(orderItemFormList);

        assertNotNull(order.getId());
    }

    @Test
    public void testHappyGetById() throws ApiException{
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

        OrderPojo orderPojo = orderService.getById(order.getId());

        assertEquals(order.getCreatedAt(), orderPojo.getCreatedAt());
    }

    @Test
    public void testSadGetById() throws ApiException{
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

        try{
            orderService.getById(20);
        }
        catch(ApiException e){
            assertEquals("Order with given id not found", e.getMessage());
        }
    }

    @Test
    public void testHappyUpdate() throws ApiException{
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

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        orderService.update(order.getId(),now);

        OrderPojo orderPojo = orderService.getById(order.getId());

        assertEquals(timestamp, orderPojo.getInvoicedCreatedAt());
    }

    @Test
    public void testhappyGetAllOrder() throws ApiException{
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
        orderDto.addOrder(orderItemFormList);
        orderDto.addOrder(orderItemFormList);

        List <OrderPojo> list = orderService.getAll();

        assertEquals(3, list.size());
    }

    @Test
    public void testHappyGetAllBetween() throws ApiException{

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

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
        OrderDetailData o = orderDto.addOrder(orderItemFormList);

        List <OrderPojo> list = orderService.getAllBetween(timestamp,o.getCreatedAt());

        System.out.println(timestamp);
        System.out.println(o.getCreatedAt());
        assertEquals(1, list.size());
    }


    @Test
    public void testHappySetInvoiceDownloaded() throws ApiException{
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

        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);
        orderService.update(order.getId(),now);

        orderService.setInvoiceDownloaded(order.getId());

        OrderPojo orderPojo = orderService.getById(order.getId());

        assertEquals(true,orderPojo.getIsInvoiceCreated());
    }
}
