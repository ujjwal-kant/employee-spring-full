package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.util.StringUtil;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;
	@Autowired
	private BrandDao branddao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo p) throws ApiException {
		normalize(p);
		if(StringUtil.isEmpty(p.getName())) {
			throw new ApiException("name cannot be empty");
		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public ProductPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<ProductPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, ProductPojo p) throws ApiException {
		normalize(p);
		ProductPojo ex = getCheck(id);
		ex.setName(p.getName());
		ex.setMrp(p.getMrp());		
		dao.update(ex);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public String getCategoryFromProductPojo(ProductPojo p) throws ApiException
	{
		int id=p.getBrand_category();
		BrandPojo d=branddao.select(id);

		if(d==null) {
			throw new ApiException("This Id doesnot exists(Product Service)");
		}
		return d.getCategory();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public String getBrandFromProductPojo(ProductPojo p) throws ApiException
	{
		int id=p.getBrand_category();
		BrandPojo d=branddao.select(id);

		if(d==null) {
			throw new ApiException("This id doesnot exists(ProductService)");
		}
		return d.getBrand();
	}

	@Transactional
	public ProductPojo getCheck(int id) throws ApiException {
		// System.out.println(id);
		ProductPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Product with given ID does not exit, id: " + id);
		}
		return p;
	}

	protected static void normalize(ProductPojo p) {
		p.setName(StringUtil.toLowerCase(p.getName()));
	}

	@Transactional
	public int getIdfrombrandANDcategory(ProductForm f) throws ApiException {
		BrandPojo p=branddao.selectBrandCategory(f.getBrand(),f.getCategory());

		if(p==null) {
			throw new ApiException("This brand and category doesnot exists(Product Service)");
		}
		return p.getId();
	}
}