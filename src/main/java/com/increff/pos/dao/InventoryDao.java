package com.increff.pos.dao;

import java.util.List;

import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.service.ApiException;


@Repository
@Transactional(rollbackOn  = ApiException.class)
public class InventoryDao extends AbstractDao {

	
    private static final String SELECT_ALL = "select i from InventoryPojo i";
    private static final String SELECT_PRODUCTID= "select i from InventoryPojo i where productId=:productId";
	// private String SELECT_BY_BARCODE= "select i from InventoryPojo i where barcode=:barcode";

	@Transactional
    public void insert(InventoryPojo inventoryPojo) {
        em.persist(inventoryPojo);
    }

	public List<InventoryPojo> selectAll() {
		TypedQuery<InventoryPojo> query = getQuery(SELECT_ALL, InventoryPojo.class);
		return query.getResultList();
	}

	public InventoryPojo selectByProductId(Integer productId) {
        TypedQuery<InventoryPojo> query = getQuery(SELECT_PRODUCTID, InventoryPojo.class);
        query.setParameter("productId", productId);
        return getSingle(query);
    }

	public void update(InventoryPojo p) {
		em.merge(p);
	}

}