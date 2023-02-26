 package com.increff.employee.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.ProductPojo;

@Repository
public class ProductDao extends AbstractDao {

	private static final String SELECT_ID = "select p from ProductPojo p where id=:id";
	private static final String SELECT_ALL = "select p from ProductPojo p";
	private static final String SEARCH_LIKE_NAME_AND_BARCODE = "select b from ProductPojo b where name like :name and barcode like :barcode";
    private static final String SELECT_BY_BARCODE = "select p from ProductPojo p where barcode=:barcode";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(ProductPojo productPojo) {
		em.persist(productPojo);
	}

	public ProductPojo select(Integer id) {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ID, ProductPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<ProductPojo> selectAll() {
		TypedQuery<ProductPojo> query = getQuery(SELECT_ALL, ProductPojo.class);
		return query.getResultList();
	}

	public ProductPojo selectByBarcode(String Barcode){
		TypedQuery<ProductPojo> query = getQuery(SELECT_BY_BARCODE, ProductPojo.class);
		query.setParameter("barcode", Barcode);
		return getSingle(query);
	}

	public void update(ProductPojo productPojo) {
		// em.merge(productPojo);
	}

    public List<ProductPojo> serachByProductNameAndBarcode(String name, String barcode) {
        TypedQuery<ProductPojo> query = getQuery(SEARCH_LIKE_NAME_AND_BARCODE, ProductPojo.class);
        query.setParameter("name",name+"%");
        query.setParameter("barcode", barcode+"%");
        return query.getResultList();
    }


	public String GetBarcodeFromProductID(OrderItemPojo p) {
		ProductPojo f=select(p.getProductId());
		return f.getBarcode();
	}

    public Integer GetProductIDFromBarcode(OrderItemForm f) {
		String Barcode=f.getBarcode();
		ProductPojo p=selectByBarcode(Barcode);
		return p.getId();

    }



}
