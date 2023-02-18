package com.increff.employee.service;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.BrandData;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.util.ConversionUtil;

@Service
public class BrandService {

	@Autowired
	private BrandDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo addBrandCategory(BrandPojo brandPojo) throws ApiException {
		checkIfBrandAndCategoryExists(brandPojo.getBrand(), brandPojo.getCategory());
		dao.insert(brandPojo);
		return brandPojo;
	}

	private void checkIfBrandAndCategoryExists(String brand, String category) throws ApiException {
		BrandPojo b = dao.selectBrandCategory(brand, category);
		if(b!=null)
		{
			throw new ApiException("Brand and Category already exists in database");
		}
	}

	@Transactional(rollbackOn = ApiException.class)
	public BrandPojo getBrandCategorybyID(Integer id) throws ApiException {
		BrandPojo brandPojo= getIfExists(id);
		return brandPojo;
	}

	public BrandPojo get(Integer id) throws ApiException {
        return getIfExists(id);
    }

	public List<BrandPojo> getByBrandAndCategory(String brand, String category) {
        if(brand.equals("") && category.equals("")) {
            return dao.selectAll();
        }
        if(brand.equals("")) {
            return dao.selectCategory(category);
        }
        if(category.equals("")) {
            return dao.selectBrand(brand);
        }
        List<BrandPojo> brandPojoList = new ArrayList<BrandPojo>();
        BrandPojo brandPojo = dao.selectBrandCategory(brand, category);
        brandPojoList.add(brandPojo);
        return brandPojoList;
    }

	@Transactional
	public List<BrandPojo> getAllBrandCategory() {
		List<BrandPojo>list1= dao.selectAll();
		return list1;
	}

	// @Transactional
	// public List<BrandPojo> selectBrand(String Brand) {
	// 	List<BrandPojo>list1= dao.selectBrand(Brand);
	// 	List<BrandData> list2 = new ArrayList<BrandData>();
	// 	for (BrandPojo p : list1) {
	// 		list2.add(ConversionUtil.getBrandData(p));
	// 	}
	// 	return list1;
	// }
	// @Transactional
	// public List<BrandData> selectCategory(String Category) {
	// 	List<BrandPojo>list1= dao.selectCategory(Category);
	// 	List<BrandData> list2 = new ArrayList<BrandData>();
	// 	for (BrandPojo p : list1) {
	// 		list2.add(ConversionUtil.getBrandData(p));
	// 	}
	// 	return list2;
	// }

	@Transactional
	public BrandData selectBrandCategory(String Brand,String Category) {
		BrandPojo b= dao.selectBrandCategory(Brand,Category);
		return ConversionUtil.getBrandData(b);
	}

	@Transactional(rollbackOn  = ApiException.class)
	public BrandPojo updateBrandCategory(Integer id, BrandPojo brandPojo) throws ApiException {
		checkIfBrandAndCategoryExists(brandPojo.getBrand(), brandPojo.getCategory());
		BrandPojo ex = getIfExists(id);
		ex.setBrand(brandPojo.getBrand());
		ex.setCategory(brandPojo.getCategory());
		dao.update(ex);
		return ex;
	}

	@Transactional
	public BrandPojo getIfNameAndCategoryExists(String brand, String category) throws ApiException {
        BrandPojo b = dao.selectBrandCategory(brand, category);
        if (b == null) {
            throw new ApiException("Brand " + brand + " with category " + category + " does not exists");
        }
        return b;
    }

	public BrandPojo getIfExists(Integer id) throws ApiException {
        BrandPojo b = dao.select(id);
        if (b == null) {
            throw new ApiException("Brand with given ID does not exist, id: " + id);
        }
        return b;
    }

    public List<BrandPojo> searchByBrandCategory(BrandPojo brandPojo) {
		List<BrandPojo> list1= dao.selectbyBrandCategorylike(brandPojo.getBrand(),brandPojo.getCategory());
		return list1;
    }
}