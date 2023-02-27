package com.increff.employee.util;

import com.increff.employee.model.*;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.model.form.OrderItemForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.model.form.SalesReportForm;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import java.util.Date;

public class NormaliseUtil {

    public static void normalizeBrandCategory(BrandForm brand) {
        brand.setBrand(StringUtil.toLowerCase(brand.getBrand()));
        brand.setCategory(StringUtil.toLowerCase(brand.getCategory()));
    }

    public static void normalizeProduct(ProductForm form) {
        Double salary = Math.round(form.getMrp() * 100.0) / 100.0;
        form.setMrp(salary);
        form.setName(StringUtil.toLowerCase(form.getName()));
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
        form.setBrand(StringUtil.toLowerCase(form.getBrand()));
        form.setCategory(StringUtil.toLowerCase(form.getCategory()));
    }


    public static void normalizeInventory(InventoryForm form) {
        form.setBarcode(StringUtil.toLowerCase(form.getBarcode()));
    }

    public static void normalizeOrderItem(List<OrderItemForm> orderItemFormList) {
        for (OrderItemForm orderItemForm : orderItemFormList) {
            orderItemForm.setSellingPrice((orderItemForm.getSellingPrice()));
            orderItemForm.setBarcode(StringUtil.toLowerCase(orderItemForm.getBarcode()));
        }
    }

    public static void normalizeSalesReport(SalesReportForm salesReportForm) {
        salesReportForm.setBrand(StringUtil.toLowerCase(salesReportForm.getBrand()));
        salesReportForm.setCategory(StringUtil.toLowerCase(salesReportForm.getCategory()));

        Date date1=new GregorianCalendar(2021, Calendar.JANUARY, 1).getTime();
        if(salesReportForm.getStartDate() == null) {
            salesReportForm.setStartDate(convertJavaDateToSqlDate(date1));
        }
        if(salesReportForm.getEndDate() == null) {
            salesReportForm.setEndDate(convertJavaDateToSqlDate(new Date()));
        }
        salesReportForm.setStartDate(convertJavaDateToSqlDate(TimeUtil.getStartOfDay(salesReportForm.getStartDate())));
        salesReportForm.setEndDate(convertJavaDateToSqlDate(TimeUtil.getEndOfDay(salesReportForm.getEndDate())));
    }

    public static java.util.Date convertFromSQLDateToJAVADate(java.sql.Date sqlDate) {
        java.util.Date javaDate = null;
        if (sqlDate != null) {
            javaDate = new Date(sqlDate.getTime());
        }
        return javaDate;
    }

    public static java.sql.Date convertJavaDateToSqlDate(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }
}

