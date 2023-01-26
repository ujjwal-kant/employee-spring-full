package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.InventoryPojo;


@Repository
public class InventoryDao extends AbstractDao {

	private static String delete_id = "delete from InventoryPojo p where id=:id";
	private static String select_id = "select p from InventoryPojo p where id=:id";
	private static String select_all = "select p from InventoryPojo p";

	private static String searchByBarcode = "select p from InventoryPojo p where barcode=:barcode";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(InventoryPojo p) {
		TypedQuery<InventoryPojo> query = getQuery(searchByBarcode, InventoryPojo.class);
		query.setParameter("barcode",p.getBarcode());
		
		// if(query==null)
		em.persist(p);
		
	}

	public int delete(int id) {
		Query query = (Query) em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public InventoryPojo select(int id) {
		TypedQuery<InventoryPojo> query = getQuery(select_id, InventoryPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(select_all, InventoryPojo.class);
		return query.getResultList();
	}

	public InventoryPojo selectByBarcode(String Barcode)
	{
		TypedQuery<InventoryPojo> query = getQuery(searchByBarcode, InventoryPojo.class);
		query.setParameter("barcode", Barcode);
		return getSingle(query);
	}

	public void update(InventoryPojo p) {
	}



}