package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SalesReportData {
    private String brand;
    private String category;
    private Integer quantity;
    private Double revenue;
    private Integer brandCategoryId;
}

