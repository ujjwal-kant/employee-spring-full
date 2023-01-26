package com.increff.employee.model;

import java.util.List;

public class OrderForm{
    private List<OrderItemForm>order;

    public List<OrderItemForm> getOrder() {
        return order;
    }

    public void setOrder(List<OrderItemForm> order) {
        this.order = order;
    }

    
}