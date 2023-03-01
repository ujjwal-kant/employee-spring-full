package com.increff.pos.util;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Date;
import java.util.List;

import com.increff.pos.model.form.BrandForm;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.model.form.SalesReportForm;
import com.increff.pos.service.ApiException;

public class ValidateUtil {
    private static Integer MAX_VALUE=1000000000;
    public static void validateProductForm(ProductForm form) throws ApiException {
        if (StringUtil.isEmpty(form.getBarcode())) {
            throw new ApiException("Barcode should not be empty.");
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
            throw new ApiException("MRP cannot be Negative");
        } 
        if(form.getMrp()>MAX_VALUE){
            throw new ApiException("MRP cannot be greater than 100000000");
        }
        if(form.getBarcode().length()>20){
            throw new ApiException("Barcode cannot be greater than 20");
        } 
        if(form.getName().length()>20){
            throw new ApiException("Name cannot be greater than 20");
        }  
    }

    public static void validateInventoryForm(InventoryForm form) throws ApiException {
        if ((form.getQuantity())==null) {
            throw new ApiException("Quantity should not be empty.");
        }
        if (form.getQuantity() < 0) {
            throw new ApiException("Quantity cannot be negative.");
        }

        if(form.getBarcode().length()>20){
            throw new ApiException("Barcode cannot be greater than 20");
        }
        if (StringUtil.isEmpty(form.getBarcode())) {
            throw new ApiException("Barcode should not be empty.");
        }
        if(form.getQuantity()>MAX_VALUE){
            throw new ApiException("MRP cannot be greater than 100000000");
        }
    }

    public static void validateOrderForm(List<OrderItemForm> orderItems) throws ApiException {
        if (orderItems == null || orderItems.isEmpty()) {
            throw new ApiException("Order items cannot be empty");
        }
        for (OrderItemForm orderItem : orderItems) {
            System.out.println(orderItem.getQuantity());
            if (orderItem.getQuantity() < 0) {
                throw new ApiException("Quantity cannot be less than 0");
            }
            if (orderItem.getSellingPrice() < 0) {
                throw new ApiException("Selling Price cannot be less than 0");
            }
            if (orderItem.getSellingPrice() > MAX_VALUE) {
                throw new ApiException("Selling Price cannot greater than 100000000");
            }
        }
    }

    public static void validateBrandForm(BrandForm brandform) throws ApiException {
        if (StringUtil.isEmpty(brandform.getBrand())) {
            throw new ApiException("Brand should not be empty.");
        }
        if (StringUtil.isEmpty(brandform.getCategory())) {
            throw new ApiException("Category should not be empty.");
        }
        if(brandform.getBrand().length()>30) {
            throw new ApiException("Brand cannot conatins more than 30 characters");
        }
        if(brandform.getCategory().length()>30) {
            throw new ApiException("Category cannot conatins more than 30 characters");
        }
    }

    public static void validateSalesReportForm(SalesReportForm salesReportForm) throws ApiException {
        String startDate = convertToString(salesReportForm.getStartDate(), "yyyy-MM-dd");
        System.out.println(startDate);
        System.out.println(salesReportForm.getStartDate());

        try {
            LocalDate.parse(startDate);
            System.out.println("Valid date");
        } catch (DateTimeParseException e) {
            throw new ApiException("Invalid Start Date");
        }

        String endDate = convertToString(salesReportForm.getEndDate(), "yyyy-MM-dd");

        try {
            LocalDate.parse(endDate);
            System.out.println("Valid date");
        } catch (DateTimeParseException e) {
            throw new ApiException("Invalid End Date");
        }
        
        LocalDate date1 = LocalDate.parse(startDate);
        LocalDate date2 = LocalDate.parse(endDate);
        
        if (date1.isAfter(date2)) {
            throw new ApiException("Start Date cannot be greater than end Date");
        }

    }

    public static String convertToString(Date date, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(date);
    }

    public static boolean isValidDate(String dateString) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // disable lenient parsing
        try {
            // parse the input string as a date using the specified format
            Date parsedDate = dateFormat.parse(dateString);
            // if parsing succeeds, the input string is a valid date in the correct format
            return true;
        } catch (Exception e) {
            // if parsing fails, the input string is not a valid date in the correct format
            return false;
        }
    }

    public static boolean isDateFormatCorrect(String dateString) {
        try {
            // Parse the string as a LocalDate with the given format yyyy-mm-dd
            LocalDate.parse(dateString, java.time.format.DateTimeFormatter.ISO_LOCAL_DATE);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    public static int compare(Date date1, String dateString2) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false); // disable lenient parsing
        try {
            // parse the input string as a date using the specified format
            Date date2 = dateFormat.parse(dateString2);
            // compare the two dates using the compareTo method
            return date1.compareTo(date2);
        } catch (Exception e) {
            // if parsing fails, return an error value (-2)
            return -2;
        }
    }
}
