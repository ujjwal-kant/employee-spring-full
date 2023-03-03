package com.increff.pos.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.DailySalesReportDao;
import com.increff.pos.pojo.DailyReportSalesPojo;

@Service
@Transactional(rollbackOn  = ApiException.class)
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
