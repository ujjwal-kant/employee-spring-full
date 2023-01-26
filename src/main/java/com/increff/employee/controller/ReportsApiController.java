package com.increff.employee.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.service.ReportsService;

import io.swagger.annotations.Api;

@Api
@RestController
public class ReportsApiController {
    
    @Autowired
    private ReportsService reportservice;

    
}
