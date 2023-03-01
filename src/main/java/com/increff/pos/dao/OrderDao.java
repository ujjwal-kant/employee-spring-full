package com.increff.pos.dao;

import java.sql.Date;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.service.ApiException;

@Repository
@Transactional(rollbackOn  = ApiException.class)
public class OrderDao extends AbstractDao {

	private static final String SELECT_ID = "select p from OrderPojo p where id=:id";
	private static final String SELECT_ALL = "select p from OrderPojo p";
	private static final String SELECT_BETWEEN_START_END_DATE = "select o from OrderPojo o where o.invoicedCreatedAt between :startingDate and :endingDate";

	@Transactional
	public OrderPojo insert(OrderPojo p) {
        em.persist(p);
        return p;
    }

	public List<OrderPojo> getAll() {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ALL,OrderPojo.class);
        return query.getResultList();
	}

	public OrderPojo getById(int id) {
		TypedQuery<OrderPojo> query = getQuery(SELECT_ID,OrderPojo.class);
		query.setParameter("id", id);
        return getSingle(query);
	}

	public List<OrderPojo> selectAllBetween(Date startingDate, Date endingDate) {
        TypedQuery<OrderPojo> query = getQuery(SELECT_BETWEEN_START_END_DATE, OrderPojo.class);
        query.setParameter("startingDate", startingDate);
        query.setParameter("endingDate", endingDate);
        return query.getResultList();
    }
    
}
