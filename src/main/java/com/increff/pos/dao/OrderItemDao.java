package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.service.ApiException;

@Repository
@Transactional(rollbackOn  = ApiException.class)
public class OrderItemDao extends AbstractDao {

    private static final String SELECT_ID= "select oi from OrderItemPojo oi where orderId=:orderId";
    private static final String SELECT_BY_ORDER_ID="select p from OrderItemPojo p where orderId=:orderId";


    @Transactional
    public void insert(OrderItemPojo orderItemPojo) {
        em.persist(orderItemPojo);
    }

    public List<OrderItemPojo> selectByOrderId(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_ID, OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        return query.getResultList();
    }

    @Transactional
    public void deleteByOrderId(Integer orderId) {
        TypedQuery<OrderItemPojo> query = getQuery(SELECT_BY_ORDER_ID,OrderItemPojo.class);
        query.setParameter("orderId", orderId);
        List<OrderItemPojo> list = query.getResultList();
        for (OrderItemPojo p : list) {
            em.remove(p);
        }
    }
}
