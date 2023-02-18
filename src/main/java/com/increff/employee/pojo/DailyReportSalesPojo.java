package com.increff.employee.pojo;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "dailysalesreport")
public class DailyReportSalesPojo {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    
    private Date date;
    @Column(nullable = false)
    private Integer invoiced_Orders_Count;
    @Column(nullable = false)
    private Integer invoiced_Items_Count;
    @Column(nullable = false)
    private Double totalRevenue;
}