package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.ReportDto;
import com.increff.employee.model.data.DailySalesReportData;
import com.increff.employee.model.data.ReportInventoryData;
import com.increff.employee.model.data.SalesReportData;
import com.increff.employee.model.form.SalesReportForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/reports")
public class ReportsApiController {
    
    @Autowired
    private ReportDto dto;

	// @ApiOperation(value = "Gets brand-category report")
    // @RequestMapping(value = "/brand", method = RequestMethod.POST)
    // public List<BrandData> brandCategoryReport(@RequestBody BrandForm brandForm) throws ApiException {
    //     return dto.getBrandCategoryReport(brandForm);
    // }

    @ApiOperation(value = "Gets report of inventory")
	@RequestMapping(path = "/inventory", method = RequestMethod.GET)
	public List<ReportInventoryData> getInventoryReport() throws ApiException { 
		return dto.getInventoryReport();
	}

	@ApiOperation(value = "Gets report of sales")
	@RequestMapping(path = "/sales", method = RequestMethod.POST)
	public List<SalesReportData> getSalesReport(@RequestBody SalesReportForm s) throws ApiException {
		return dto.getSalesReport(s);
	}

	@ApiOperation(value = "Gets report of daily-sales")
	@RequestMapping(path = "/daily-sales", method = RequestMethod.GET)
	public List<DailySalesReportData> getDailySalesReport() throws ApiException { 
		return dto.getDailySalesReport();
	}

	@ApiOperation(value = "Generate daily report for rest day")
	@RequestMapping(path = "/generate-rest-day", method = RequestMethod.POST)
	public List<DailySalesReportData> generateReportForRestDay() throws ApiException { 
		return dto.generateReportForRestDay();
	}
    
}
