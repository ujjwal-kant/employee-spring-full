package com.increff.employee.dto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.employee.model.data.PdfData;
import com.increff.employee.model.data.PdfListData;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Service
public class PdfDto {
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public void get(int id) throws IOException, ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.get(id);
        if(orderItemPojoList.size()==0)
            throw new ApiException("no order items present in order to place");

        PdfData pdfData = new PdfData();
        // Date now=Date.now();
        LocalDateTime now = LocalDateTime.now();
        orderService.update(id,now);
        List<PdfListData> pdfListData = new ArrayList<>();
        Integer c = 0;
        Double total = 0.0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            c++;
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            total +=convertOrderItemPojoToPdfData(orderItemPojo,pdfListData,pdfData,c,productPojo);
        }
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(date);
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(time);
        pdfData.setInvoiceTime(formattedDate);
        pdfData.setInvoiceDate(formattedTime);
        pdfData.setOrderId(id);

        double val = total;
        val = val*100;
        val = Math.round(val);
        val = val /100;

        pdfData.setTotal(val);
        System.out.println(val);
        pdfData.setItemList(pdfListData);

        // DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // String formattedDate = LocalDateTime.now().format(date);
        // DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        // String formattedTime = LocalDateTime.now().format(time);
        // pdfData.setInvoiceTime(formattedTime);
        // pdfData.setInvoiceDate(formattedDate);
        // pdfData.setOrderId(id);
        // pdfData.setTotal(total);
        // pdfData.setItemList(pdfListData);


        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/invoice/api/pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(pdfData);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        String response = restTemplate.postForObject(url, request, String.class);

        String filePath = "C:/Rep/employee/invoices\\order_"+id+".pdf";
        byte[] decodedBytes = Base64.getDecoder().decode(response);
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(decodedBytes);
        fileOutputStream.close();
    }

    public static Double convertOrderItemPojoToPdfData(OrderItemPojo orderItemPojo, List<PdfListData> list, PdfData pdfData, Integer integer, ProductPojo productPojo) {
        PdfListData pdfListData = new PdfListData();
        pdfListData.setSno(integer);
        pdfListData.setBarcode(productPojo.getBarcode());
        pdfListData.setProduct(productPojo.getName());
        pdfListData.setQuantity(orderItemPojo.getQuantity());
        pdfListData.setUnitPrice(orderItemPojo.getSellingPrice());
        Double v = orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
        pdfListData.setAmount(v);
        list.add(pdfListData);
        return v ;
    }

    public ResponseEntity<byte[]> download(int id) throws ApiException, IOException {
        Path pdf = Paths.get("C:/Rep/employee/invoices/order_" + id + ".pdf");
        byte[] contents = Files.readAllBytes(pdf);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "order_" + id + ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }
}




