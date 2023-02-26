package com.increff.employee.dto;

import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.DailySalesReportData;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ReportInventoryData;
import com.increff.employee.model.SalesReportData;
import com.increff.employee.model.SalesReportForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.DailyReportSalesPojo;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;
import com.increff.employee.service.ReportsService;
import com.increff.employee.util.ConversionUtil;
import com.increff.employee.util.NormaliseUtil;
import com.increff.employee.util.TimeUtil;

@Component
public class ReportDto {

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ReportsService reportService;

    public List<ReportInventoryData> getInventoryReport() throws ApiException {
        List<ReportInventoryData>list= new ArrayList<ReportInventoryData>();
        List<BrandPojo>allBrandCategory=brandService.getAllBrandCategory();
        List<BrandData>allBrand = new ArrayList<BrandData>();
		for (BrandPojo p : allBrandCategory) {
			allBrand.add(ConversionUtil.getBrandData(p));
		}
        HashMap<Integer,Integer> map = new HashMap<>();
        List<ProductPojo>list1=productService.getAllProduct();
        List<ProductData>allProduct = new ArrayList<ProductData>();
		for (ProductPojo p : list1) {
            BrandPojo b = brandService.get(p.getBrand_category_id());
            allProduct.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));

		}
        for(ProductData p:allProduct)
        {
            ProductPojo productPojo =productService.getByBarcode(p.getBarcode());
            Integer brandCategoryId=productPojo.getBrand_category_id();
            Integer quantity=inventoryService.getByProductId(productPojo.getId()).getQuantity();
            if (map.containsKey(brandCategoryId))
            {
                int x = map.get(brandCategoryId) + quantity;
                map.replace(brandCategoryId, x);
            }
            else
            {
                map.put(brandCategoryId,quantity);
            }
        }
        for(BrandData b: allBrand)
        {
            ReportInventoryData p= new ReportInventoryData();
            p.setBrand(b.getBrand());
            p.setCategory(b.getCategory());
            if(map.containsKey(b.getId()))
            p.setQuantity(map.get(b.getId()));
            else
            p.setQuantity(0);

            list.add(p);
        }
        return list;
    }

    // public List<SalesReportData> getSalesReport(SalesReportForm s) throws ApiException {
    //     List<SalesReportData>list=new ArrayList<SalesReportData>();
    //     // System.out.println(s.getEndDate());
    //     // List<OrderItemData>allOrderBetweenDates=new ArrayList<OrderItemData>();
    //     // List<OrderPojo> orderPojo=orderService.getAllOrderBetweenDate(s.getStartDate(),s.getEndDate());
    //     // for(OrderPojo Pojo:orderPojo)
    //     // {
    //     //     List<OrderItemPojo> orderItemPojo=orderitemService.get(Pojo);
    //     //     for(OrderItemPojo orderitempojo:orderItemPojo)
    //     //     {
    //     //         ProductData d=productService.get(orderitempojo.getProductId());
    //     //         ProductPojo p=productService.convert(d);
    //     //         String barcode=p.getBarcode();
    //     //         // System.out.println(orderitempojo.getId());
    //     //         allOrderBetweenDates.add(orderService.convertOrderItemPojoToOrderItemData(orderitempojo,barcode));
    //     //     }
    //     // }

    //     // String brand=s.getBrand();
    //     // String category=s.getCategory();
    //     // List<BrandData>BrandcategoryList=new ArrayList<BrandData>();
    //     // if(brand=="All" && category=="All")
    //     // {
    //     //     BrandcategoryList=brandservice.getAll();
    //     // }
    //     // else if(brand=="All")
    //     // {
    //     //     BrandcategoryList=brandservice.selectCategory(category);
    //     // }
    //     // else if(category=="All")
    //     // {
    //     //     BrandcategoryList=brandservice.selectBrand(brand);
    //     // }
    //     // else
    //     // {
    //     //     BrandData brandData=brandservice.selectBrandCategory(brand, category);
    //     //     BrandcategoryList.add(brandData);
    //     // }
    //     // Map<Integer,Pair<String,String>>brandmap= new HashMap<Integer,Pair<String,String>>();
    //     // for(BrandData b:BrandcategoryList)
    //     // {
    //     //     System.out.println(b.getId());
    //     //     if(!brandmap.containsKey(b.getId()))
    //     //     {
    //     //         Pair<String,String>bc=new Pair<String,String>(b.getBrand(),b.getCategory());
    //     //         brandmap.put(b.getId(),bc);
    //     //     }
    //     // }
    //     // // System.out.println("_______________");
    //     // Map<Integer,Pair<Integer,Double>>map= new HashMap<Integer,Pair<Integer,Double>>();
    //     // for(OrderItemData list1: allOrderBetweenDates)
    //     // {
    //     //     String barcode=list1.getBarcode();
    //     //     ProductData p=productService.getBybarcode(barcode);

    //     //     if(brandmap.containsKey(p.getBrand_category_id()))
    //     //     {
    //     //         if(!map.containsKey(p.getBrand_category_id()))
    //     //         {
    //     //             Pair<Integer,Double>pair= new Pair<Integer,Double>(list1.getQuantity(),list1.getQuantity()*list1.getMrp());
    //     //             map.put(p.getBrand_category_id(),pair);
    //     //         }
    //     //         else
    //     //         {
    //     //             Pair<Integer,Double>pair=map.get(p.getBrand_category_id());
    //     //             Pair<Integer,Double>updatedPair=new Pair<Integer,Double>(pair.getKey()+list1.getQuantity(),pair.getValue()+list1.getQuantity()*list1.getMrp());
    //     //             map.put(p.getBrand_category_id(),updatedPair);
    //     //         }
    //     //     }
    //     // }

    //     // for(BrandData b:BrandcategoryList)
    //     // {
    //     //     if(brandmap.containsKey(b.getId()))
    //     //     {
    //     //         SalesReportData salesReportData=new SalesReportData();

    //     //         Pair<String,String>bc=brandmap.get(b.getId());
    //     //         Pair<Integer,Double>qr=map.get(b.getId());

    //     //         salesReportData.setBrand(bc.getKey());
    //     //         salesReportData.setCategory(bc.getValue());
    //     //         salesReportData.setQuantity(qr.getKey());
    //     //         salesReportData.setRevenue(qr.getValue());
                

    //     //         list.add(salesReportData);
    //     //     }
    //     // }
        
        
    //     return list;
    // }

    private List<SalesReportData> initializeSalesReportData(List<BrandPojo> brandPojoList) {
        List<SalesReportData> salesReportDataList = new ArrayList<SalesReportData>();
        for(BrandPojo brandPojo : brandPojoList) {
            SalesReportData salesReportData = new SalesReportData();
            salesReportData.setBrand(brandPojo.getBrand());
            salesReportData.setCategory(brandPojo.getCategory());
            salesReportData.setBrandCategoryId(brandPojo.getId());
            salesReportData.setQuantity(0);
            salesReportData.setRevenue(0.0);
            salesReportDataList.add(salesReportData);
        }
        return salesReportDataList;
    }

    private List<ProductPojo> getProductListFromOrderItems(List<OrderItemPojo> orderItemPojoList) throws ApiException {
        List<ProductPojo> productPojoList = new ArrayList<ProductPojo>();
        for(OrderItemPojo orderItemPojo : orderItemPojoList) {
            ProductPojo productPojo = productService.getIfExists(orderItemPojo.getProductId());
            productPojoList.add(productPojo);
        }
        return productPojoList;
    }

    public List<SalesReportData> calculateQuantityAndRevenue(List<BrandPojo> brandPojoList, List<ProductPojo> productPojoList, List<OrderItemPojo> orderItemPojoList) {
        List<SalesReportData> salesReportDataList = initializeSalesReportData(brandPojoList);
        for (OrderItemPojo orderItem : orderItemPojoList) {
            int productId = orderItem.getProductId();
            ProductPojo product = productPojoList.stream().filter(p -> p.getId() == productId).findFirst().get();
            int brandCategoryId = product.getBrand_category_id();
            for (SalesReportData salesReportItemDataItem : salesReportDataList) {
                if (salesReportItemDataItem.getBrandCategoryId() == brandCategoryId) {
                    salesReportItemDataItem.setQuantity(salesReportItemDataItem.getQuantity() + orderItem.getQuantity());
                    salesReportItemDataItem.setRevenue(salesReportItemDataItem.getRevenue()+orderItem.getQuantity() * orderItem.getSellingPrice());
                }
            }
        }
        return salesReportDataList;
    }

    public List<SalesReportData> getSalesReport(SalesReportForm salesReportForm) throws ApiException {
        NormaliseUtil.normalizeSalesReport(salesReportForm);
        validateSalesReportForm(salesReportForm);
        // System.out.println(salesReportForm.getEndDate());


        List<BrandPojo> brandPojoList = brandService.getByBrandAndCategory(salesReportForm.getBrand(), salesReportForm.getCategory());
        initializeSalesReportData(brandPojoList);
        Date yesterday1=(Date) TimeUtil.getStartOfDay(salesReportForm.getStartDate());
        Date date1=(Date) TimeUtil.getEndOfDay(salesReportForm.getEndDate());
        List<OrderItemPojo> orderItemPojoList = orderItemService.getAllOrderItemBetween(yesterday1, date1);
        System.out.print(orderItemPojoList.size());
        List<ProductPojo> productPojoList = getProductListFromOrderItems(orderItemPojoList);
        return calculateQuantityAndRevenue(brandPojoList, productPojoList, orderItemPojoList);
    }


    private void validateSalesReportForm(SalesReportForm salesReportForm) {
        //how to check if date is in correct format or not
    }

    // public List<SalesReportData> getDailyReport() {
    //     return null;
    // }

    // public List<BrandData> getBrandCategoryReport(BrandForm brandForm) {
    //     return null;
    //     //happening from brandpojo directly
    // }

    public List<DailySalesReportData> getDailySalesReport() throws ApiException {
        // generateDailySalesReport();
        // System.out.print("Hello from getDailySlesReport" );
        List<DailySalesReportData> dailySalesReportDataList = new ArrayList<DailySalesReportData>();
        List<DailyReportSalesPojo> dailySalesReportPojoList = reportService.getAll();
        for(DailyReportSalesPojo dailySalesReportPojo : dailySalesReportPojoList) {
            dailySalesReportDataList.add(ConversionUtil.getDailySalesReportData(dailySalesReportPojo));
        }
        // System.out.print("Hello from getDailySlesReport" );
        return dailySalesReportDataList;
    }
    // (cron = "0 0 0 * * ?")
    // @Scheduled(cron = "0 * * * * *")
    // @Transactional(rollbackOn = ApiException.class)
    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() throws ApiException {
        // System.out.print("Hello from  schedular");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = TimeUtil.getStartOfDay(cal.getTime());

        // System.out.print(yesterday);
        // System.out.print(new Date());
        // List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, new Date())
        getDailyReport(yesterday, new Date());
    }

    public List<DailySalesReportData> generateReportForRestDay() throws ApiException {
        DailyReportSalesPojo lastdailyReportGeneratedOn=reportService.lastdailyReportGeneratedOn();
        
        Date last=TimeUtil.getStartOfDay(lastdailyReportGeneratedOn.getDate());
        // System.out.println(last);

        // Calendar cal = Calendar.getInstance();
        Date today=TimeUtil.getStartOfDay(new Date());
        // System.out.println(today);

        Integer cnt=0;
        
        while(today.compareTo(last)!=0)
        {
            long time3=last.getTime();
            long time4=time3+ 24*60*60*1000;
            Date next=new Date(time4);
            getDailyReport(last,next);
            last=next;

            // System.out.println(next);
            // System.out.println("+");
            // System.out.println(today);

            cnt++;
            if(cnt>10)
            break;
        }
        return null;
    }

    public void getDailyReport(Date date1,Date date2) throws ApiException{
        List<OrderPojo> orderPojoList = orderService.getAllBetween(date1, date2);
        Integer count=0;
        List<OrderItemPojo> orderItemPojoList = orderItemService.getAllOrderItemBetween(date1, date2);

        Double totalRevenue = 0d;
        for (OrderItemPojo orderItem : orderItemPojoList) {
            // System.out.print(orderItem.getId());
            count++;
            totalRevenue += orderItem.getSellingPrice() * orderItem.getQuantity();
        }

        DailyReportSalesPojo dailySalesReportPojo = new DailyReportSalesPojo();
        dailySalesReportPojo.setDate(date2);
        dailySalesReportPojo.setTotalRevenue(totalRevenue);
        dailySalesReportPojo.setInvoiced_Items_Count(orderPojoList.size());
        dailySalesReportPojo.setInvoiced_Orders_Count(count);
        reportService.add(dailySalesReportPojo);
    }
}
