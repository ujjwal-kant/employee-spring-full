package com.increff.employee.dao;

import java.util.List;

import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;

@Repository
@Transactional(rollbackOn  = ApiException.class)
public class UserDao extends AbstractDao {

	private static final String DELETE_ID = "delete from UserPojo p where id=:id";
	private static final String SELECT_BY_ID = "select p from UserPojo p where id=:id";
	private static final String SELECT_BY_EMAIL = "select p from UserPojo p where email=:email";
	private static final String SLEECT_ALL = "select p from UserPojo p";

	
	@Transactional
	public void insert(UserPojo userPojo) {
		em().persist(userPojo);
	}

	public int delete(Integer id) {
		Query query = em().createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public UserPojo select(Integer id) {
		TypedQuery<UserPojo> query = getQuery(SELECT_BY_ID, UserPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public UserPojo select(String email) {
		TypedQuery<UserPojo> query = getQuery(SELECT_BY_EMAIL, UserPojo.class);
		query.setParameter("email", email);
		return getSingle(query);
	}

	public List<UserPojo> selectAll() {
		TypedQuery<UserPojo> query = getQuery(SLEECT_ALL, UserPojo.class);
		return query.getResultList();
	}

	public void update(UserPojo userPojo) {
		em.merge(userPojo);
	}


}
