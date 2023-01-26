package com.increff.employee.model;

import java.util.List;

public class OrderItemsForm {
    private List<OrderItemForm>ListOfItems;

    public List<OrderItemForm> getListOfItems() {
        return ListOfItems;
    }

    public void setListOfItems(List<OrderItemForm> listOfItems) {
        ListOfItems = listOfItems;
    }
}
