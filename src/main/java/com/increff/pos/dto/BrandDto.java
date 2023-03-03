package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.HelperUtil;
import com.increff.pos.util.NormaliseUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class BrandDto {

    @Autowired
    private BrandService brandService;

    @Transactional(rollbackOn = ApiException.class)
    public BrandData addBrandCategory(BrandForm form) throws ApiException {
        NormaliseUtil.normalizeBrandCategory(form);
        ValidateUtil.validateBrandForm(form);
        BrandPojo brandPojo = brandService.addBrandCategory(ConversionUtil.getBrandPojo(form));
        return ConversionUtil.getBrandData(brandPojo);
    }

    @Transactional(rollbackOn = ApiException.class)
    public BrandData updateBrandCategory(Integer id, BrandForm form) throws ApiException {
        NormaliseUtil.normalizeBrandCategory(form);
        ValidateUtil.validateBrandForm(form);
        BrandPojo brandPojo = brandService.updateBrandCategory(id,ConversionUtil.getBrandPojo(form));
        return ConversionUtil.getBrandData(brandPojo);
    }

    public List<BrandData> getAllBrandCategory() {
        List<BrandPojo>list1= brandService.getAllBrandCategory();
        List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo p : list1) {
			list2.add(ConversionUtil.getBrandData(p));
		}
		return list2;
    }

    public BrandData getBrandCategoryById(Integer id) throws ApiException {
        BrandPojo brandPojo= brandService.getBrandCategorybyID(id);
        return ConversionUtil.getBrandData(brandPojo);
    }
    
    public List<BrandData> searchByBrandCategory(BrandForm form) {
        NormaliseUtil.normalizeBrandCategory(form);
        HelperUtil.setBrandForm(form);
        List<BrandPojo> list1=brandService.searchByBrandCategory(ConversionUtil.getBrandPojo(form));
        List<BrandData> list2 = new ArrayList<BrandData>();
		for (BrandPojo brandPojo : list1) {
			list2.add(ConversionUtil.getBrandData(brandPojo));
		}
        return list2;
    }
}
