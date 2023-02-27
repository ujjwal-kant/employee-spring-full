package com.increff.employee.model.data;

import java.sql.Timestamp;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class OrderData {

    private Integer id;
    private Timestamp createdAt;
    private Double billAmount;
    private Boolean isInvoiceCreated;
}