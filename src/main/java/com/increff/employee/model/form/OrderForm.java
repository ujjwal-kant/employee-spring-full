package com.increff.employee.model.form;

import lombok.Getter;
import lombok.Setter;

import java.util.List;


@Getter
@Setter
public class OrderForm {
    private List<OrderItemForm> orderItems;
}