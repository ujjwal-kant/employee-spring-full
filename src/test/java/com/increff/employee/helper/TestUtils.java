package com.increff.employee.helper;

import java.util.ArrayList;
import java.util.List;

import com.increff.employee.model.form.OrderItemForm;

public class TestUtils {
    public static List<OrderItemForm> getOrderItemArray(List<String>barcodes,List<Integer>quantities, List<Double>sellingPrices) {
        List<OrderItemForm> orderItemFormList = new ArrayList<>();
        for (int i=0; i<barcodes.size(); i++) {
            OrderItemForm orderItemForm = new OrderItemForm();
            orderItemForm.setBarcode(barcodes.get(i));
            orderItemForm.setQuantity(quantities.get(i));
            orderItemForm.setSellingPrice(sellingPrices.get(i));
            orderItemFormList.add(orderItemForm);
        }
        return orderItemFormList;
    }
}
