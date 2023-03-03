package com.increff.pos.dto;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
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
import com.increff.pos.model.data.PdfData;
import com.increff.pos.model.data.PdfListData;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;


@Service
public class PdfDto {
    @Autowired
    private OrderItemService orderItemService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    public void get(int id) throws IOException, ApiException {
        orderService.getById(id);
        List<OrderItemPojo> orderItemPojoList = orderItemService.get(id);
        if(orderItemPojoList.size()==0){
            throw new ApiException("No order items present in order to place");
        }

        PdfData pdfData = new PdfData();
        LocalDateTime now = LocalDateTime.now();
        Timestamp timestamp = Timestamp.valueOf(now);

        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String formattedDate = LocalDateTime.now().format(date);
        DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        String formattedTime = LocalDateTime.now().format(time);

        DateTimeFormatter address = DateTimeFormatter.ofPattern("HH-mm-ss");
        String add = LocalDateTime.now().format(address);

        add+=formattedDate;

        orderService.update(id,timestamp,add);

        List<PdfListData> pdfListData = new ArrayList<>();
        Integer c = 0;
        Double total = 0.0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            c++;
            ProductPojo productPojo = productService.get(orderItemPojo.getProductId());
            total +=convertOrderItemPojoToPdfData(orderItemPojo,pdfListData,pdfData,c,productPojo);
        }
        pdfData.setInvoiceTime(formattedTime);
        pdfData.setInvoiceDate(formattedDate);
        pdfData.setOrderId(id);

        double val = total;
        val = val*100;
        val = Math.round(val);
        val = val /100;

        pdfData.setTotal(val);
        pdfData.setItemList(pdfListData);

        try{
        RestTemplate restTemplate = new RestTemplate();
        String url = "http://localhost:8000/invoice/api/pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String payload = objectMapper.writeValueAsString(pdfData);
        HttpEntity<String> request = new HttpEntity<>(payload, headers);
        String response = restTemplate.postForObject(url, request, String.class);

        String filePath = "C:/Rep/employee/invoices\\order_"+add+".pdf";
        // System.out.println(filePath);
        byte[] decodedBytes = Base64.getDecoder().decode(response);
        FileOutputStream fileOutputStream = new FileOutputStream(filePath);
        fileOutputStream.write(decodedBytes);
        fileOutputStream.close();
        }
        catch(Exception e){
            throw new ApiException("Invoice Server not started");
        }
    }

    public ResponseEntity<byte[]> download(int id) throws ApiException, IOException {
        OrderPojo orderPojo = orderService.getById(id);
        Path pdf = Paths.get("C:/Rep/employee/invoices/order_" +orderPojo.getPath()+".pdf");
        byte[] contents = Files.readAllBytes(pdf);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        String filename = "order_" + orderPojo.getPath()+ ".pdf";
        headers.setContentDispositionFormData(filename, filename);
        headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
        ResponseEntity<byte[]> response = new ResponseEntity<>(contents, headers, HttpStatus.OK);
        return response;
    }

    public static Double convertOrderItemPojoToPdfData(OrderItemPojo orderItemPojo, List<PdfListData> list, PdfData pdfData, Integer integer, ProductPojo productPojo) {
        PdfListData pdfListData = new PdfListData();
        pdfListData.setSno(integer);
        pdfListData.setBarcode(productPojo.getBarcode());
        pdfListData.setProduct(productPojo.getName());
        pdfListData.setQuantity(orderItemPojo.getQuantity());
        pdfListData.setUnitPrice(orderItemPojo.getSellingPrice());
        Double v = orderItemPojo.getQuantity() * orderItemPojo.getSellingPrice();
        DecimalFormat df = new DecimalFormat("#.##");
        Double roundedNum = Double.parseDouble(df.format(v));
        pdfListData.setAmount(roundedNum);
        list.add(pdfListData);
        return roundedNum;
    }
    
}




