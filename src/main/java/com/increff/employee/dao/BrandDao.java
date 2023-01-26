package com.increff.employee.dao;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.springframework.stereotype.Repository;

import com.increff.employee.pojo.BrandPojo;

@Repository
public class BrandDao extends AbstractDao {

	private static String delete_id = "delete from BrandPojo p where id=:id";
	private static String select_id = "select p from BrandPojo p where id=:id";
	private static String select_all = "select p from BrandPojo p";
	private static String search = "select p from BrandPojo p where brand=:brand and category=:category";
	
	// private static String brand_CategoryId = "select p from BrandPojo p where brand=:brand and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandPojo p) {
//		System.out.println(search);
		TypedQuery<BrandPojo> query = getQuery(search, BrandPojo.class);
		query.setParameter("brand", p.getBrand());
        query.setParameter("category", p.getCategory());
        List<BrandPojo> list = query.getResultList();
        if (list.size() == 0) {
        	em.persist(p);
        }		
	}

	public int delete(int id) {
		Query query = em.createQuery(delete_id);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public BrandPojo select(int id) {
		TypedQuery<BrandPojo> query = getQuery(select_id, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(select_all, BrandPojo.class);
		return query.getResultList();
	}
	
	public BrandPojo selectBrandCategory(String brand,String category) {
        TypedQuery<BrandPojo> query = getQuery(search, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

	public void update(BrandPojo p) {
	}



}