package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.OrderItemPojo;

@Repository
public class OrderItemDao extends AbstractDao {

    private static String delete_id = "delete from OrderItemPojo p where id=:id";
	private static String select_id = "select p from OrderItemPojo p where orderid=:orderid";
	private static String select_all = "select p from OrderItemPojo p";

    @PersistenceContext
	private EntityManager em;
    
    public List<OrderItemPojo> GetOrderItemPOjoById(int orderid) {
        TypedQuery<OrderItemPojo> query = getQuery(select_id,OrderItemPojo.class);
        query.setParameter("orderid",orderid);
        return query.getResultList();
    }
    
}
