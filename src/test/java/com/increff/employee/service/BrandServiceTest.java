package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

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

        BrandPojo b = brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());

        assertEquals(brandPojo.getBrand(), b.getBrand());
        assertEquals(brandPojo.getCategory(), b.getCategory());
    }

    @Test
    public void testSadAddBrandCategoryIfAlreadyExists() throws ApiException{
        BrandPojo brandPojo=DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        try{
            brandService.addBrandCategory(brandPojo);
        }
        catch(ApiException e){
            assertEquals("Brand and Category already exists in database", e.getMessage());
        }

    }

    @Test
    public void testHappyGetBrandCategoryByID() throws ApiException{
        BrandPojo brandPojo=DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        BrandPojo dummyBrand = brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());
        Integer Id=dummyBrand.getId();

        BrandPojo brandPojoById = brandService.getBrandCategorybyID(Id);

        assertEquals(brandPojo.getBrand(), brandPojoById.getBrand());
        assertEquals(brandPojo.getCategory(), brandPojoById.getCategory());


    }

    @Test
    public void testSadGetBrandCategoryByID() throws ApiException{
        BrandPojo brandPojo=DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());
        Integer Id=20;

        try{
            brandService.getBrandCategorybyID(Id);
        }
        catch(ApiException e){
            assertEquals("Brand with given ID does not exist, id: " + Id,e.getMessage());
        }
    }

    @Test
    public void testHappyGetAllBrand() throws ApiException{
        BrandPojo brandPojo=DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        BrandPojo brandPojo1=DummyBrandForm("brand1", "category1");
        brandService.addBrandCategory(brandPojo1);

        List<BrandPojo> getAllBrandCategory = brandService.getAllBrandCategory();
        

        assertEquals(2, getAllBrandCategory.size());
    }

    @Test
    public void testHappyUpdateBrandCatgeory() throws ApiException{
        BrandPojo brandPojo = DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        BrandPojo dummyBrand = brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());
        Integer Id=dummyBrand.getId();

        BrandPojo brandPojo1 = DummyBrandForm("brand1", "category1");

        BrandPojo b = brandService.updateBrandCategory(Id, brandPojo1);

        assertEquals(brandPojo1.getBrand(), b.getBrand());
        assertEquals(brandPojo1.getCategory(), b.getCategory());
    }

    @Test
    public void testSadUpdateBrandCatgeory() throws ApiException{
        BrandPojo brandPojo = DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        BrandPojo dummyBrand = brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());
        Integer Id=dummyBrand.getId();

        BrandPojo brandPojo1 = DummyBrandForm("brand", "category");

        try{
            brandService.updateBrandCategory(Id, brandPojo1);
        }
        catch(ApiException e){
            assertEquals("Brand and Category already exists in database",e.getMessage());
        }
    }
    

    @Test
    public void testHappysearchByBrandCategory() throws ApiException{
        BrandPojo brandPojo = DummyBrandForm("brand", "category");
        brandService.addBrandCategory(brandPojo);

        BrandPojo dummyBrand = brandService.getIfBrandAndCategoryExists(brandPojo.getBrand(),brandPojo.getCategory());
        Integer Id=dummyBrand.getId();

        BrandPojo brandPojo1 = DummyBrandForm("brand1", "category1");
        brandService.addBrandCategory(brandPojo1);

        BrandPojo brandPojoSearch = DummyBrandForm("b", "");

        List<BrandPojo> list = brandService.searchByBrandCategory(brandPojoSearch);

        assertEquals(2, list.size());
    }

    @Test
    public void testHappyGetByBrandAndCategoryselectAll() throws ApiException{
        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("brand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }

        List<BrandPojo> list = brandService.getByBrandAndCategory("","");

        assertEquals(10, list.size());
    }

    @Test
    public void testHappyGetByBrandAndCategoryselectCategory() throws ApiException{
        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("brand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }
        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("vrand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }

        List<BrandPojo> list = brandService.getByBrandAndCategory("","category1");

        assertEquals(2, list.size());
    }

    @Test
    public void testHappyGetByBrandAndCategoryselectBrand() throws ApiException{
        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("brand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }

        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("brand"+i, "tategory"+i);
            brandService.addBrandCategory(brandPojo);
        }

        List<BrandPojo> list = brandService.getByBrandAndCategory("brand5","");

        assertEquals(2, list.size());
    }

    @Test
    public void testHappyGetByBrandAndCategoryselectBrandCategory() throws ApiException{
        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("brand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }

        for(int i=0; i<10 ; i++){
            BrandPojo brandPojo = DummyBrandForm("vrand"+i, "category"+i);
            brandService.addBrandCategory(brandPojo);
        }

        List<BrandPojo> list = brandService.getByBrandAndCategory("vrand4","c");

        assertEquals(1, list.size());
    }


    
}
