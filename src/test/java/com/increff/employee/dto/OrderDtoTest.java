package com.increff.employee.dto;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.helper.TestUtils;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderDetailData;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;

public class OrderDtoTest extends AbstractUnitTest{
    
    @Autowired
    private OrderDto orderDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

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
    public void testHappyAddOrder() throws ApiException{
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

        assertNotNull(order.getId());
    }

    @Test
    public void testSadAddOrder() throws ApiException{

        try{    
            List<String> barcodes = new ArrayList<String>();
            List<Integer>quantities = new ArrayList<Integer>();
            List<Double>sellingPrices = new ArrayList<Double>();

            List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
            orderDto.addOrder(orderItemFormList);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Order items cannot be empty",e.getMessage());
        }

        try{   
            List<String> barcodes = new ArrayList<String>();
            List<Integer>quantities = new ArrayList<Integer>();
            List<Double>sellingPrices = new ArrayList<Double>();

            barcodes.add("barcode1");
            quantities.add(-1);
            sellingPrices.add(1.0);

            List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
            orderDto.addOrder(orderItemFormList);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Quantity cannot be less than or equal to 0",e.getMessage());
        }

        try{    
            List<String> barcodes = new ArrayList<String>();
            List<Integer>quantities = new ArrayList<Integer>();
            List<Double>sellingPrices = new ArrayList<Double>();

            barcodes.add("barcode1");
            quantities.add(1);
            sellingPrices.add(-1.0);


            List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
            orderDto.addOrder(orderItemFormList);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Selling Price cannot be less than 0",e.getMessage());
        }

        try{
            List<String> barcodes = new ArrayList<String>();
            List<Integer>quantities = new ArrayList<Integer>();
            List<Double>sellingPrices = new ArrayList<Double>();

            barcodes.add("barcode2");
            quantities.add(1);
            sellingPrices.add(1.0);

            barcodes.add("barcode2");
            quantities.add(1);
            sellingPrices.add(1.0);

            List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
            orderDto.addOrder(orderItemFormList);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Order items multiple instance of barcode barcode2",e.getMessage());
        }

        try{
            List<String> barcodes = new ArrayList<String>();
            List<Integer>quantities = new ArrayList<Integer>();
            List<Double>sellingPrices = new ArrayList<Double>();

            barcodes.add("barcode2");
            quantities.add(1);
            sellingPrices.add(1879.0);

            List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
            orderDto.addOrder(orderItemFormList);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Selling Price higher than MRP(INR 2.98 ) for barcode2",e.getMessage());
        }
    }

    @Test
    public void testHappyGetOrderDetails() throws ApiException{
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

        OrderDetailData check=orderDto.getOrderDetails(order.getId());

        assertEquals(order.getBillAmount(), check.getBillAmount());
        assertEquals(order.getCreatedAt(), check.getCreatedAt());
    }

    @Test
    public void testSadGetOrderDetails() throws ApiException{
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

        Integer id=10;

        try{orderDto.getOrderDetails(id);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Order with given id not found",e.getMessage());
        }                        
    }

    @Test
    public void testHappyGetAllOrders() throws ApiException{
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

        List<OrderData>allOrder=orderDto.getAllOrders();

        assertEquals(3, allOrder.size());
    }

    @Test
    public void testHappyUpdateOrder() throws ApiException{
        List<String> barcodes = new ArrayList<String>();
        barcodes.add("barcode1");
        List<Integer>quantities = new ArrayList<Integer>();
        quantities.add(6);
        List<Double>sellingPrices = new ArrayList<Double>();
        sellingPrices.add(1.0);

        List<OrderItemForm> orderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        OrderDetailData order = orderDto.addOrder(orderItemFormList);

        Integer id = order.getId();

        barcodes.add("barcode2");
        quantities.add(1);
        sellingPrices.add(1.0);

        List<OrderItemForm> newOrderItemFormList = TestUtils.getOrderItemArray(barcodes,quantities,sellingPrices);
        
        orderDto.updateOrder(id,newOrderItemFormList);

        OrderDetailData orderDetail = orderDto.getOrderDetails(id);

        assertEquals(orderDetail.getOrderItems().get(0).getBarcode(), newOrderItemFormList.get(0).getBarcode());
        assertEquals(orderDetail.getOrderItems().get(0).getQuantity(), newOrderItemFormList.get(0).getQuantity());
        assertEquals(orderDetail.getOrderItems().get(0).getSellingPrice(), newOrderItemFormList.get(0).getSellingPrice());
    }

}

