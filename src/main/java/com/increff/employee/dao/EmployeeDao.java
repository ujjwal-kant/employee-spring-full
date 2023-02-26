package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.EmployeePojo;

@Repository
public class EmployeeDao extends AbstractDao {

	private static final String DELETE_ID = "delete from EmployeePojo p where id=:id";
	private static final String SELECT_ID = "select p from EmployeePojo p where id=:id";
	private static final String SELECT_ALL = "select p from EmployeePojo p";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(EmployeePojo p) {
		em.persist(p);
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public EmployeePojo select(int id) {
		TypedQuery<EmployeePojo> query = getQuery(SELECT_ID, EmployeePojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<EmployeePojo> selectAll() {
		TypedQuery<EmployeePojo> query = getQuery(SELECT_ALL, EmployeePojo.class);
		return query.getResultList();
	}

	public void update(EmployeePojo p) {
	}



}
