package com.increff.pos.dto;

import java.util.Date;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.data.DailySalesReportData;
import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.data.ReportInventoryData;
import com.increff.pos.model.data.SalesReportData;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.DailyReportSalesPojo;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.service.ReportsService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormaliseUtil;
import com.increff.pos.util.TimeUtil;
import com.increff.pos.util.ValidateUtil;

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
            BrandPojo b = brandService.getBrandCategorybyID(p.getBrandCategoryId());
            allProduct.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));

		}
        for(ProductData p:allProduct)
        {
            ProductPojo productPojo =productService.getByBarcode(p.getBarcode());
            Integer brandCategoryId=productPojo.getBrandCategoryId();
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
            int brandCategoryId = product.getBrandCategoryId();
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
        ValidateUtil.validateSalesReportForm(salesReportForm);
        List<BrandPojo> brandPojoList = brandService.getByBrandAndCategory(salesReportForm.getBrand(), salesReportForm.getCategory());
        initializeSalesReportData(brandPojoList);
        Date yesterday1=(Date) TimeUtil.getStartOfDay(salesReportForm.getStartDate());
        Date date1=(Date) TimeUtil.getEndOfDay(salesReportForm.getEndDate());
        List<OrderItemPojo> orderItemPojoList = orderItemService.getAllOrderItemBetween(yesterday1, date1);
        // System.out.print(orderItemPojoList.size());
        List<ProductPojo> productPojoList = getProductListFromOrderItems(orderItemPojoList);
        return calculateQuantityAndRevenue(brandPojoList, productPojoList, orderItemPojoList);
    }

    public List<DailySalesReportData> getDailySalesReport() throws ApiException {
        List<DailySalesReportData> dailySalesReportDataList = new ArrayList<DailySalesReportData>();
        List<DailyReportSalesPojo> dailySalesReportPojoList = reportService.getAll();
        for(DailyReportSalesPojo dailySalesReportPojo : dailySalesReportPojoList) {
            dailySalesReportDataList.add(ConversionUtil.getDailySalesReportData(dailySalesReportPojo));
        }
        return dailySalesReportDataList;
    }

    public List<DailySalesReportData> generateReportForRestDay() throws ApiException {
        DailyReportSalesPojo lastdailyReportGeneratedOn=reportService.lastdailyReportGeneratedOn();
        // System.out.println(lastdailyReportGeneratedOn.getDate());
        
        Date last=TimeUtil.getStartOfDay(lastdailyReportGeneratedOn.getDate());
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
            count+=orderItem.getQuantity();
            totalRevenue += orderItem.getSellingPrice() * orderItem.getQuantity();
        }
        DecimalFormat df = new DecimalFormat("#.##");
        Double roundedTotal = Double.parseDouble(df.format(totalRevenue));

        DailyReportSalesPojo dailySalesReportPojo = new DailyReportSalesPojo();
        dailySalesReportPojo.setDate(date2);
        dailySalesReportPojo.setTotalRevenue(roundedTotal);
        dailySalesReportPojo.setInvoicedItemsCount(count);
        dailySalesReportPojo.setInvoicedOrdersCount(orderItemPojoList.size());
        reportService.add(dailySalesReportPojo);
    }
}
