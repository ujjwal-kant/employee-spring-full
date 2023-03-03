package com.increff.pos.util;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.ProductForm;

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
