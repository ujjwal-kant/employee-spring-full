package com.increff.employee.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.DailySalesReportDao;
import com.increff.employee.pojo.DailyReportSalesPojo;

@Service
public class ReportsService {

    @Autowired
    private DailySalesReportDao dao;

    public List<DailyReportSalesPojo> getAll() {
        return  dao.selectAll();
    }

    public void add(DailyReportSalesPojo dailySalesReportPojo) {
        dao.insert(dailySalesReportPojo);
    }

    public DailyReportSalesPojo lastdailyReportGeneratedOn() {
        return dao.getLastDay();
    }
    
}
