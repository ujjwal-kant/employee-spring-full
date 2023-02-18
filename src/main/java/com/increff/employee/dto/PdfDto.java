package com.increff.employee.dto;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.model.PdfData;
import com.increff.employee.model.PdfListData;
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
    private OrderService orderService;

    @Autowired
    private ProductService productService;
    
    public PdfData get(int id) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.get(id);
        if(orderItemPojoList.size()==0)
            throw new ApiException("no order items present in order to place");

        PdfData pdfData = new PdfData();
        //LocalDateTime now = LocalDateTime.now();
//        orderPojo.setDate(now);
        // orderService.update(id,now);
        List<PdfListData> pdfListData = new ArrayList<>();
        Integer c = 0;
        Double total = 0.0;
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            c++;
            ProductPojo productPojo = productService.getCheck(orderItemPojo.getProductId());
            total +=convertOrderItemPojoToPdfData(orderItemPojo,pdfListData,pdfData,c,productPojo);
        }
        // DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        // String formattedDate = LocalDateTime.now().format(date);
        // DateTimeFormatter time = DateTimeFormatter.ofPattern("HH:mm:ss");
        // String formattedTime = LocalDateTime.now().format(time);
        pdfData.setInvoiceTime("");
        pdfData.setInvoiceDate("");
        pdfData.setOrderId(id);
        pdfData.setTotal(total);
        pdfData.setItemList(pdfListData);
        return pdfData;
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
        return v;
    }
}

