
package com.increff.employee.service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.OrderDao;
import com.increff.employee.pojo.OrderPojo;


@Service
public class OrderService {

    @Autowired
    private OrderDao dao;

    @Transactional(rollbackOn = ApiException.class)
    public OrderPojo createNewOrder() {
        OrderPojo orderPojo = new OrderPojo();
        return dao.insert(orderPojo);
    }

    @Transactional
    public OrderPojo getById(Integer id) throws ApiException {
        OrderPojo orderPojo = dao.getById(id);
        if (orderPojo == null) {
            throw new ApiException("Order with given id not found");
        }
        return orderPojo;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(int id,LocalDateTime time) throws ApiException {
        OrderPojo ex = getById(id);
        ex.setIsInvoiceCreated(true);
        Timestamp timestamp = Timestamp.valueOf(time);
        ex.setInvoicedCreatedAt(timestamp);
    }

    public List<OrderPojo> getAll() {
        return dao.getAll();
    }

    public List<OrderPojo> getAllBetween(java.util.Date yesterday, java.util.Date date) {
        java.sql.Date startingDate1 = new java.sql.Date(yesterday.getTime());
        java.sql.Date endingDate1 = new java.sql.Date(date.getTime());
        return dao.selectAllBetween(startingDate1, endingDate1);
    }

    @Transactional(rollbackOn = ApiException.class)
    public void setInvoiceDownloaded(Integer id) throws ApiException {
        OrderPojo orderPojo = getById(id);
        orderPojo.setIsInvoiceCreated(true);
    }
}
