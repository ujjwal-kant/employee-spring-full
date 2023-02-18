package com.increff.employee.util;

import java.util.List;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.model.ProductForm;
import com.increff.employee.service.ApiException;

public class ValidateUtil {
    public static void validateProductForm(ProductForm form) throws ApiException {
        if (StringUtil.isEmpty(form.getBarcode())) {
            throw new ApiException("BarCode should not be empty.");
        }
        if (StringUtil.isEmpty(form.getBrand())) {
            throw new ApiException("Brand should not be empty.");
        }
        if (StringUtil.isEmpty(form.getCategory())) {
            throw new ApiException("Category should not be empty.");
        }
        if (StringUtil.isEmpty(form.getName())) {
            throw new ApiException("Name should not be empty.");
        }
        if(form.getMrp()<0){
            throw new ApiException("Mrp cannot be Negative");
        } 
    }

    public static void validateInventoryForm(InventoryForm form) throws ApiException {
        if ((form.getQuantity())==null) {
            throw new ApiException("Quantity should not be empty.");
        }
        if (form.getQuantity() < 0) {
            throw new ApiException("Quantity cannot be negative");
        }
        
        if (StringUtil.isEmpty(form.getBarcode())) {
            throw new ApiException("BarCode should not be empty.");
        }
        
    }

    public static void validateOrderForm(List<OrderItemForm> orderItems) throws ApiException {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new ApiException("Order items cannot be empty");
        }
        for (OrderItemForm orderItem : orderItems) {
            if (orderItem.getQuantity() <= 0) {
                throw new ApiException("Quantity cannot be less than or equal to 0");
            }
            if (orderItem.getSellingPrice() < 0) {
                throw new ApiException("Selling Price cannot be less than 0");
            }
        }
    }

    public static void validateBrandForm(BrandForm brandform) throws ApiException {
        if (StringUtil.isEmpty(brandform.getBrand())) {
            throw new ApiException("Brand  should not be empty.");
        }
        if (StringUtil.isEmpty(brandform.getCategory())) {
            throw new ApiException("category  should not be empty.");
        }
    }
}
