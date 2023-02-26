package com.increff.employee.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandPojo;

public class BrandServiceTest extends AbstractUnitTest{

    @Autowired
    private BrandService brandService;

    protected BrandPojo DummyBrandForm(String Brand,String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(Brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }
    
    @Test
    public void testHappyAddBrandCategory() throws ApiException{
        BrandPojo brandPojo=DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);
    }
}
