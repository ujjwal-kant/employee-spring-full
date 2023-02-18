package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.util.ConversionUtil;
import com.increff.employee.util.HelperUtil;
import com.increff.employee.util.NormaliseUtil;
import com.increff.employee.util.StringUtil;
import com.increff.employee.util.ValidateUtil;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo addBrandCategory(BrandForm form) throws ApiException {
        NormaliseUtil.normalizeBrandCategory(form);
        ValidateUtil.validateBrandForm(form);
        return brandService.addBrandCategory(ConversionUtil.getBrandPojo(form));
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandPojo updateBrandCategory(Integer id, BrandForm form) throws ApiException {
        NormaliseUtil.normalizeBrandCategory(form);
        ValidateUtil.validateBrandForm(form);
        return brandService.updateBrandCategory(id,ConversionUtil.getBrandPojo(form));
    }

    @Transactional
    public List<BrandData> getAllBrandCategory() {
        List<BrandPojo>list1= brandService.getAllBrandCategory();
        List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list1) {
			list2.add(ConversionUtil.getBrandData(p));
		}
		return list2;
    }

    @Transactional
    public BrandData getBrandCategoryById(Integer id) throws ApiException {
        BrandPojo brandPojo= brandService.getBrandCategorybyID(id);
        return ConversionUtil.getBrandData(brandPojo);
    }
    
    public List<BrandData> searchByBrandCategory(BrandForm form) {
        NormaliseUtil.normalizeBrandCategory(form);
        HelperUtil.setBrandForm(form);
        List<BrandPojo>list1=brandService.searchByBrandCategory(ConversionUtil.getBrandPojo(form));
        List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo brandPojo : list1) {
			list2.add(ConversionUtil.getBrandData(brandPojo));
		}
        return list2;
    }
}
