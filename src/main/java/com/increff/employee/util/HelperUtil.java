package com.increff.employee.util;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductForm;

public class HelperUtil {

    public static void setBrandForm(BrandForm form) {
        if (form.getBrand() == null) {
            form.setBrand("");
        }
        if (form.getCategory() == null) {
            form.setCategory("");
        }
    }

    public static void setProductForm(ProductForm form) {
        if (form.getBarcode() == null) {
            form.setBarcode("");
        }
        if (form.getName() == null) {
            form.setName("");
        }
    }
    
}
