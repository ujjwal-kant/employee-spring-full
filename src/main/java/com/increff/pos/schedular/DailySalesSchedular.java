package com.increff.pos.schedular;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.service.ApiException;
import com.increff.pos.util.TimeUtil;

@Component
public class DailySalesSchedular {

    @Autowired
    private ReportDto reportDto;

    @Scheduled(cron = "0 0 0 * * ?")
    public void generateDailySalesReport() throws ApiException {
        // System.out.print("Hello from  schedular");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        Date yesterday = TimeUtil.getStartOfDay(cal.getTime());

        // System.out.print(yesterday);
        // System.out.print(new Date());
        // List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, new Date())
        reportDto.getDailyReport(yesterday, new Date());
    }
}