package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OrderForm {
    private List<OrderItemForm> orderItems;
}