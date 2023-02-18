package com.increff.employee.model;

import java.sql.Date;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SalesReportForm {
    private String brand;
    private String category;
    private Date startDate;
    private Date endDate;
}
