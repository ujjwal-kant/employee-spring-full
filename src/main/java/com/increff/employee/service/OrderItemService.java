package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;

@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao orderitemdao;

    public List<OrderItemPojo> get(OrderPojo Pojo) {
        // System.out.println(Pojo.getId());
        return orderitemdao.GetOrderItemPOjoById(Pojo.getId());
    }
    
}
