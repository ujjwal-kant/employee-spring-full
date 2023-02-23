package com.increff.employee.dto; 

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.util.StringUtil;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandService brandService;

    protected BrandForm DummyBrandForm(String Brand,String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(Brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    @Test
    public void testHappyAddBrandCategory() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("AdDidas ");
        brandForm.setCategory("  SpoRT   ");

        brandDto.addBrandCategory(brandForm);

        BrandPojo brandPojo=brandService.getIfBrandAndCategoryExists("addidas","sport");
            
        assertEquals(brandPojo.getBrand(),StringUtil.toLowerCase(brandForm.getBrand()));
        assertEquals(brandPojo.getCategory(),StringUtil.toLowerCase(brandForm.getCategory()));
    }

    @Test
    public void testSadAddBrandCategory() throws ApiException{
		BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);


        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("Nike");
            brandForm1.setCategory("  spOrt");

            brandDto.addBrandCategory(brandForm1);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Brand and Category already exists in database",e.getMessage());
        }

        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("");
            brandForm1.setCategory(" dskg ");

            brandDto.addBrandCategory(brandForm1);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Brand should not be empty.",e.getMessage());
        }

        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("glds");
            brandForm1.setCategory("     ");

            brandDto.addBrandCategory(brandForm1);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Category should not be empty.",e.getMessage());
        }
    }

    // public void testUpdateBrandCategory() throws ApiException{

    // }

    @Test
    public void testHappyUpdateBrandCategory() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandPojo b=brandDto.addBrandCategory(brand);
        Integer id=b.getId();

        BrandPojo p=brandDto.updateBrandCategory(id,brandForm);

        assertEquals(brandForm.getBrand(), p.getBrand());
        assertEquals(brandForm.getCategory(), p.getCategory());
    }

    @Test
    public void testSadUpdateBrandCategory() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandPojo b=brandDto.addBrandCategory(brand);
        Integer id=b.getId();

        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("Nike");
            brandForm1.setCategory("  spOrt");

            brandDto.addBrandCategory(brandForm1);

            brandDto.updateBrandCategory(id,brandForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Brand and Category already exists in database",e.getMessage());
        }

        id=20;
        try{
            
            brandDto.getBrandCategoryById(id);
        }
        catch(ApiException e){
            assertEquals("Brand with given ID does not exist, id: " + id,e.getMessage());
        }


    }

    @Test 
    public void testHappygetBrandCategorybyId() throws ApiException{
        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandPojo brandPojo = brandDto.addBrandCategory(brand);

        BrandData brandData=brandDto.getBrandCategoryById(brandPojo.getId());

        assertEquals(brandData.getBrand(), brand.getBrand());
        assertEquals(brandData.getCategory(), brand.getCategory());
    }

    @Test 
    public void testSadgetBrandCategorybyId() throws ApiException{
        Integer id=20;
        try{
        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        brandDto.addBrandCategory(brand);
        }
        catch(ApiException e){
        brandDto.getBrandCategoryById(id);
        assertEquals("Brand with given ID does not exist, id: " + id,e.getMessage());
        }
    }

    @Test
    public void testgetAllBrandCategory() throws ApiException{
        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        brandDto.addBrandCategory(brand);

        BrandForm brandForm = new BrandForm();
        brandForm .setBrand("Nike");
        brandForm .setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);

        List<BrandData>list=brandDto.getAllBrandCategory();

        assertEquals(list.get(0).getBrand(), brand.getBrand());
        assertEquals(list.get(0).getCategory(), brand.getCategory());

        assertEquals(list.get(1).getBrand(), brandForm.getBrand());
        assertEquals(list.get(1).getCategory(), brandForm.getCategory());
    }

    @Test
    public void testsearchByBrandCategory() throws ApiException{
        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        brandDto.addBrandCategory(brand);
        
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("  Add");
        brandForm.setCategory("  s");

        List<BrandData> listOfBrandData = brandDto.searchByBrandCategory(brand);

        assertEquals(listOfBrandData.get(0).getBrand(), brand.getBrand());
        assertEquals(listOfBrandData.get(0).getCategory(), brand.getCategory());

   }

}