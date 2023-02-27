package com.increff.employee.util;

import com.increff.employee.model.*;
import com.increff.employee.model.data.BrandData;
import com.increff.employee.model.data.DailySalesReportData;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.data.OrderData;
import com.increff.employee.model.data.OrderDetailData;
import com.increff.employee.model.data.OrderItemData;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.data.UserData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.UserForm;
import com.increff.employee.pojo.*;

import java.util.List;

import org.springframework.security.crypto.bcrypt.BCrypt;


public class ConversionUtil {

    // BrandPOJO -> BrandData
    public static BrandData getBrandData(BrandPojo p) {
        BrandData data = new BrandData();
        data.setId(p.getId());
        data.setBrand(p.getBrand());
        data.setCategory(p.getCategory());
        return data;
    }

    // BrandForm -> BrandPOJO
    public static BrandPojo getBrandPojo(BrandForm f) {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(f.getBrand());
        brandPojo.setCategory(f.getCategory());
        return brandPojo;
    }

    // ProductPOJO -> ProductData
    public static ProductData getProductData(ProductPojo p, String brandName, String brandCategory) {
        ProductData data = new ProductData();
        data.setId(p.getId());
        data.setName(p.getName());
        data.setMrp(p.getMrp());
        data.setBarcode(p.getBarcode());
        data.setBrand(brandName);
        data.setCategory(brandCategory);
        return data;
    }

    // ProductForm -> ProductPOJO
    public static ProductPojo getProductPojo(ProductForm f, Integer brandId) {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(f.getName());
        productPojo.setMrp(f.getMrp());
        productPojo.setBarcode(f.getBarcode());
        productPojo.setBrand_category_id(brandId);
        return productPojo;
    }

    // InventoryPOJO -> InventoryData
    public static InventoryData getInventoryData(InventoryPojo i, String name, String barcode) {
        InventoryData data = new InventoryData();
        data.setProductName(name);
        data.setBarcode(barcode);
        data.setQuantity(i.getQuantity());
        return data;
    }

    // InventoryForm -> InventoryPOJO
    public static InventoryPojo getInventoryPojo(InventoryForm f, Integer productId) {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setProductId(productId);
        inventoryPojo.setQuantity(f.getQuantity());
        return inventoryPojo;
    }

    // OrderItemPOJO -> OrderItemData
    public static OrderItemData getOrderItemData(OrderItemPojo o, ProductPojo p) {
        OrderItemData data = new OrderItemData();
        data.setId(o.getId());
        data.setOrderId(o.getOrderId());
        data.setBarcode(p.getBarcode());
        data.setQuantity(o.getQuantity());
        data.setProductName(p.getName());
        data.setSellingPrice(o.getSellingPrice());
        return data;
    }

    // OrderItemForm -> OrderItemPOJO
    public static OrderItemPojo getOrderItemPojo(OrderItemForm f, Integer orderId, Integer productId) {
        OrderItemPojo orderItemPojo = new OrderItemPojo();
        orderItemPojo.setOrderId(orderId);
        orderItemPojo.setProductId(productId);
        orderItemPojo.setQuantity(f.getQuantity());
        orderItemPojo.setSellingPrice(f.getSellingPrice());
        return orderItemPojo;
    }

    // OrderPOJO -> OrderData
    public static OrderData getOrderData(OrderPojo p, List<OrderItemPojo> orderItems) {
        OrderData orderData = new OrderData();
        orderData.setId(p.getId());
        orderData.setCreatedAt(p.getCreatedAt());
        // System.out.println(p.getCreatedAt());

        double billAmount = 0;
        for(OrderItemPojo orderItem : orderItems) {
            billAmount += orderItem.getQuantity() * orderItem.getSellingPrice();
        }

        orderData.setIsInvoiceCreated(p.getIsInvoiceCreated());
        orderData.setBillAmount(billAmount);
        return orderData;
    }

    public static OrderDetailData getOrderDetailsData(OrderData orderData, List<OrderItemData> orderItems) {
        OrderDetailData orderDetailData = new OrderDetailData();
        orderDetailData.setOrderItems(orderItems);
        orderDetailData.setId(orderData.getId());
        orderDetailData.setCreatedAt(orderData.getCreatedAt());
        orderDetailData.setBillAmount(orderData.getBillAmount());
        orderDetailData.setIsInvoiceCreated(orderData.getIsInvoiceCreated());
        return orderDetailData;
    }

    public static DailySalesReportData getDailySalesReportData(DailyReportSalesPojo dailySalesReportPojo) {
        DailySalesReportData dailySalesReportData = new DailySalesReportData();
        dailySalesReportData.setDate(dailySalesReportPojo.getDate());
        dailySalesReportData.setOrderCount(dailySalesReportPojo.getInvoiced_Items_Count());
        dailySalesReportData.setItemCount(dailySalesReportPojo.getInvoiced_Orders_Count());
        dailySalesReportData.setTotalRevenue(dailySalesReportPojo.getTotalRevenue());
        return dailySalesReportData;
    }

    // UserForm -> UserPojo
    public static UserPojo getUserPojoFromForm(UserForm userForm) {
        UserPojo user = new UserPojo();
        userForm.setPassword(hashPassword(userForm.getPassword()));
        user.setEmail(userForm.getEmail());
        user.setRole(userForm.getRole());
        user.setPassword(userForm.getPassword());
        return user;
    }

    private static String hashPassword(String plainTextPassword){
        return BCrypt.hashpw(plainTextPassword, BCrypt.gensalt());
    }

    public static UserData getUserDataFromPojo(UserPojo user) {
        UserData data = new UserData();
        data.setEmail(user.getEmail());
        data.setRole(user.getRole());
        data.setId(user.getId());
        return data;
    }

}
