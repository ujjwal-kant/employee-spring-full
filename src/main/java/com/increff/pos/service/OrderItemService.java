package com.increff.pos.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.pos.dao.OrderItemDao;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class OrderItemService {

    @Autowired
    private OrderItemDao dao;

    @Autowired
    private OrderService orderService;

    public List<OrderItemPojo> get(Integer orderId) throws ApiException {
        return dao.selectByOrderId(orderId);
    }

    public void deleteByOrderId(Integer orderId) {
        dao.deleteByOrderId(orderId);
    }

    // @Transactional(rollbackOn = ApiException.class)
    // public void insertMultiple(List<OrderItemPojo> orderItemPojos) {
    //     for (OrderItemPojo orderItemPojo : orderItemPojos) {
    //         dao.insert(orderItemPojo);
    //     }
    // }

    public List<OrderItemPojo> getAllOrderItemBetween(java.util.Date yesterday, java.util.Date date) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        List<OrderPojo> orderPojoList = orderService.getAllBetween(yesterday, date);
        for(OrderPojo orderPojo : orderPojoList) {
            List<OrderItemPojo> orderItemPojo = get(orderPojo.getId());
            orderItemPojoList.addAll(orderItemPojo);
        }
        return orderItemPojoList;
    }

    public OrderItemPojo insert(OrderItemPojo orderItemPojo) {
        dao.insert(orderItemPojo);
        return orderItemPojo;
    }

    // public List<OrderItemPojo> getByOrderId(int orderId) {
    //     return dao.selectByOrderId(orderId);
    // }
}
