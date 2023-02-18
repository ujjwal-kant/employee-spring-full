package com.increff.employee.service;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderItemDao;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.util.TimeUtil;


@Service
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;
    @Autowired
    private OrderService orderService;

    public List<OrderItemPojo> get(Integer orderId) throws ApiException {
        return dao.selectByOrderId(orderId);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void deleteByOrderId(Integer orderId) {
        dao.deleteByOrderId(orderId);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void insertMultiple(List<OrderItemPojo> orderItemPojos) {
        for (OrderItemPojo orderItemPojo : orderItemPojos) {
            dao.insert(orderItemPojo);
        }
    }

    public List<OrderItemPojo> getAllOrderItemBetween(java.util.Date yesterday, java.util.Date date) throws ApiException {
        // java.sql.Date startingDate1 = new java.sql.Date(yesterday.getTime());
        // java.sql.Date endingDate1 = new java.sql.Date(date.getTime());
        // Date yesterday1=(Date) TimeUtil.getStartOfDay(yesterday);
        // Date date1=(Date) TimeUtil.getStartOfDay(date);
        System.out.println(yesterday);

        System.out.println(date);

        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, date);
        for(OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojo = get(orderPojo.getId());
            orderItemPojoList.addAll(orderItemPojo);
        }
        return orderItemPojoList;
    }

    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        dao.insert(orderItemPojo);
    }

    public List<OrderItemPojo> getByOrderId(int orderId) {
        return dao.selectByOrderId(orderId);
    }
}

