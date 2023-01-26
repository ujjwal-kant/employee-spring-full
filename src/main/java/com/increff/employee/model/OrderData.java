package com.increff.employee.model;

import java.util.List;

public class OrderData{

    private int id;
    private String createdAt;
    private int totalAmount;
    private List<OrderItemData>orders;
    
    public int getId() {
        return id;
    }
    public List<OrderItemData> getOrders() {
        return orders;
    }
    public void setOrders(List<OrderItemData> orders) {
        this.orders = orders;
    }
    public void setId(int id) {
        this.id = id;
    }
    
    public int getTotalAmount() {
        return totalAmount;
    }
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    public String getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
    
}