package com.increff.employee.controller;



import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.PdfDto;
import com.increff.employee.model.PdfData;
import com.increff.employee.service.ApiException;
import com.increff.employee.util.Invoice1.PDF_Generator;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;




@Api
@RestController
@RequestMapping("/api/pdf")
public class PdfApiController {

    @Autowired
    private PdfDto dto;
    @ApiOperation(value = "Generates PDF")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public void get(@PathVariable int id) throws ApiException {
        PDF_Generator pdf_generator = new PDF_Generator();
        PdfData pdfData = dto.get(id);
        pdf_generator.pdf_generator(pdfData);
    }

    @ApiOperation(value = "generate pdf")
    @RequestMapping(path = "/download/{id}", method = RequestMethod.GET)
    public ResponseEntity<byte[]> download(@PathVariable int id) throws ApiException, IOException {
        Path pdf = Paths.get("./src/main/resources/apache/PdfFile/" + id + "_invoice.pdf");

        byte[] contents = Files.readAllBytes(pdf);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);

        String filename = "Order" + id + "_invoice.pdf";
        headers.setContentDispositionFormData(filename, filename);

        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
        
    }
    
}
