package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class DailySalesReportData {

    private Date date;
    private Integer orderCount;
    private Integer itemCount;
    private Double totalRevenue;
}
