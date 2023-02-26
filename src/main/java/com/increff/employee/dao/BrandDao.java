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

	private static final String DELETE_ID = "delete from BrandPojo p where id=:id";
	private static final String SELECT_ID = "select p from BrandPojo p where id=:id";
	private static final String SELECT_ALL = "select p from BrandPojo p";
	private static final String SEARCH_BY_BRAND_AND_CATEGORY = "select p from BrandPojo p where brand=:brand and category=:category";
	private static final String SEARCH_BY_BRAND = "select p from BrandPojo p where brand=:brand";
	private static final String SEARCH_BY_CATEGORY = "select p from BrandPojo p where category=:category";
	private static final String SELECT_LIKE_BARND_CATEGORY = "select b from BrandPojo b where brand like :brand and category like :category";
	// private static String brand_CategoryId = "select p from BrandPojo p where brand=:brand and category=:category";

	@PersistenceContext
	private EntityManager em;

	@Transactional
	public void insert(BrandPojo p) {
        em.persist(p);		
	}

	public int delete(int id) {
		Query query = em.createQuery(DELETE_ID);
		query.setParameter("id", id);
		return query.executeUpdate();
	}

	public BrandPojo select(Integer id) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ID, BrandPojo.class);
		query.setParameter("id", id);
		return getSingle(query);
	}

	public List<BrandPojo> selectAll() {
		TypedQuery<BrandPojo> query = getQuery(SELECT_ALL, BrandPojo.class);
		return query.getResultList();
	}
	
	public BrandPojo selectBrandCategory(String brand,String category) {
        TypedQuery<BrandPojo> query = getQuery(SEARCH_BY_BRAND_AND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand);
        query.setParameter("category", category);
        return getSingle(query);
    }

	public List<BrandPojo> selectBrand(String brand) {
        TypedQuery<BrandPojo> query = getQuery(SEARCH_BY_BRAND, BrandPojo.class);
        query.setParameter("brand", brand);
        return query.getResultList();
    }

	public List<BrandPojo> selectCategory(String category) {
        TypedQuery<BrandPojo> query = getQuery(SEARCH_BY_CATEGORY, BrandPojo.class);
        query.setParameter("category", category);
        return query.getResultList();
    }

	public void update(BrandPojo p) {
	}

	public List<BrandPojo> selectbyBrandCategorylike(String brand, String category) {
		TypedQuery<BrandPojo> query = getQuery(SELECT_LIKE_BARND_CATEGORY, BrandPojo.class);
        query.setParameter("brand", brand+"%");
        query.setParameter("category", category+"%");
        return query.getResultList();
	}



}