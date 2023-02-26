package com.increff.employee.controller;



import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.PdfDto;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class PdfApiController {

    @Autowired
    private PdfDto dto;

    @ApiOperation(value = "Generates PDF")
    @RequestMapping(path = "/api/pdf/{id}", method = RequestMethod.GET)
    public void get(@PathVariable int id) throws ApiException, IOException {
        dto.get(id);
    }

    @ApiOperation(value = "generate pdf")
    @RequestMapping(path = "/api/pdf/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable int id) throws ApiException, IOException {
        return dto.download(id);
    }

}
