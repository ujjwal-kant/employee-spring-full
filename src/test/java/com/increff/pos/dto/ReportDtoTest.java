package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.helper.TestUtils;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.data.OrderDetailData;
import com.increff.pos.model.data.ReportInventoryData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailyReportSalesPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ReportsService;

public class ReportDtoTest extends AbstractUnitTest {
    
    @Autowired
    private ReportDto reportDto;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private OrderDto orderDto;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PdfDto pdfDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private ReportsService reportService;

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
    public void testHappyGetInventoryReport() throws ApiException{
        List<ReportInventoryData>list = reportDto.getInventoryReport();
        Integer total=25;
        assertEquals(1, list.size());
        assertEquals(list.get(0).getBrand(), "brand");
        assertEquals(list.get(0).getCategory(), "category");
        assertEquals(list.get(0).getQuantity(), total);
    }

    @Test
     public void  testHappyGetSalesReport() throws ApiException, IOException{
        SalesReportForm salesReportForm = new SalesReportForm();
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonthDate = currentDate.plusMonths(1);
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        Date endDate = java.sql.Date.valueOf(nextMonthDate);
        Date startDate = java.sql.Date.valueOf(previousMonthDate);

        salesReportForm.setBrand("");
        salesReportForm.setCategory("");
        salesReportForm.setEndDate((java.sql.Date) endDate);
        salesReportForm.setStartDate((java.sql.Date) startDate);

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

        LocalDateTime localDateTime = currentDate.atStartOfDay();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        OrderPojo orderPojo = orderService.getById(order.getId());
        orderPojo.setIsInvoiceCreated(true);
        orderPojo.setInvoicedCreatedAt(timestamp);

        List<SalesReportData> list=  reportDto.getSalesReport(salesReportForm);

        Double revenue= 0.0;
        Integer quantity= 0;
        for(OrderItemForm orderItemForm: orderItemFormList){
            revenue+=orderItemForm.getQuantity()*orderItemForm.getSellingPrice();
            quantity+=orderItemForm.getQuantity();
        }

        assertEquals(quantity, list.get(0).getQuantity());
        assertEquals(revenue, list.get(0).getRevenue());
        
    }

    @Test
    public void  testSadGetSalesReportIfStartDateGreaterThanEndDate() throws ApiException, IOException{
        SalesReportForm salesReportForm = new SalesReportForm();
        LocalDate currentDate = LocalDate.now();
        LocalDate nextMonthDate = currentDate.plusMonths(1);
        LocalDate previousMonthDate = currentDate.minusMonths(1);
        Date endDate = java.sql.Date.valueOf(previousMonthDate);
        Date startDate = java.sql.Date.valueOf(nextMonthDate);

        salesReportForm.setBrand("");
        salesReportForm.setCategory("");
        salesReportForm.setEndDate((java.sql.Date) endDate);
        salesReportForm.setStartDate((java.sql.Date) startDate);

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
        orderPojo.setIsInvoiceCreated(true);
        

        try{
            List<SalesReportData> list=  reportDto.getSalesReport(salesReportForm);
        }
        catch(ApiException e){
            assertEquals("Start Date cannot be greater than end Date", e.getMessage());
        }
        
    }

    @Test
    public void testHappyGetDailySalesReport() throws ApiException, IOException{
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

        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        Timestamp timestamp = Timestamp.valueOf(localDateTime);

        OrderPojo orderPojo = orderService.getById(order.getId());
        orderPojo.setIsInvoiceCreated(true);
        orderPojo.setInvoicedCreatedAt(timestamp);

        
        LocalDate nextDate = currentDate.plusMonths(1);
        LocalDate previousDate = currentDate.minusMonths(1);
        Date endDate = java.sql.Date.valueOf(nextDate);
        Date startDate = java.sql.Date.valueOf(previousDate);

        reportDto.getDailyReport(startDate,endDate);
        DailyReportSalesPojo d=reportService.lastdailyReportGeneratedOn();
        reportDto.generateReportForRestDay();

        List<DailyReportSalesPojo>list1= reportService.getAll();

        List<DailySalesReportData> list = reportDto.getDailySalesReport();

        Double revenue= 0.0;
        Integer quantity= 0;
        
        reportService.add(list1.get(0));
        for(OrderItemForm orderItemForm: orderItemFormList){
            revenue+=orderItemForm.getQuantity()*orderItemForm.getSellingPrice();
            quantity+=orderItemForm.getQuantity();
        }
        // System.out.println(qntity);
        assertEquals(revenue, list.get(0).getTotalRevenue());
        assertEquals(quantity, list.get(0).getItemCount());
        assertEquals(revenue, list1.get(0).getTotalRevenue());
        assertEquals(quantity, list1.get(0).getInvoicedItemsCount());
    }
}
