package com.increff.pos.dto; 

import static org.junit.Assert.assertEquals;

import java.util.List;

import com.increff.pos.dto.BrandDto;
import com.increff.pos.model.data.BrandData;
import com.increff.pos.model.form.BrandForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.util.StringUtil;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandService brandService;

    protected BrandForm brandForm(String brand,String category){
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand(brand);
        brandForm.setCategory(category);
        return brandForm;
    }

    @Test
    public void testHappyAddBrandCategory() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("AdDidas ");
        brandForm.setCategory("  SpoRT   ");

        BrandData brandData = brandDto.addBrandCategory(brandForm);
            
        assertEquals(brandData.getBrand(),StringUtil.toLowerCase(brandForm.getBrand()));
        assertEquals(brandData.getCategory(),StringUtil.toLowerCase(brandForm.getCategory()));
    }

    @Test
    public void testSadAddBrandCategoryIfBrandCategoryAlreadyExists() throws ApiException{
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
            assertEquals("Brand and Category already exists in database",e.getMessage());
        }
    }


    @Test
    public void testSadAddBrandCategoryIfBrandIsEmpty() throws ApiException{
        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("");
            brandForm1.setCategory(" dskg ");
    
            brandDto.addBrandCategory(brandForm1);
        }
        catch(ApiException e){
            assertEquals("Brand should not be empty.",e.getMessage());
        }
    }

    @Test
    public void testSadAddBrandCategoryIfCategoryIsEmpty() throws ApiException{
        try{
            BrandForm brandForm1 = new BrandForm();
            brandForm1.setBrand("glds");
            brandForm1.setCategory("     ");
    
            brandDto.addBrandCategory(brandForm1);
        }
        catch(ApiException e){
            assertEquals("Category should not be empty.",e.getMessage());
        }
    }

    @Test
    public void testHappyUpdateBrandCategory() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandData brandData=brandDto.addBrandCategory(brand);
        Integer id = brandData.getId();

        BrandData updateBrandData=brandDto.updateBrandCategory(id,brandForm);

        assertEquals(brandForm.getBrand(), updateBrandData.getBrand());
        assertEquals(brandForm.getCategory(), updateBrandData.getCategory());
    }

    @Test
    public void testSadUpdateBrandCategoryIfBrandCategoryAlreadyExists() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandData brandData = brandDto.addBrandCategory(brand);
        Integer id = brandData.getId();

        try{
            BrandForm newBrandForm = new BrandForm();
            newBrandForm.setBrand("Nike");
            newBrandForm.setCategory("  spOrt");

            brandDto.addBrandCategory(newBrandForm);

            brandDto.updateBrandCategory(id,newBrandForm);
        }
        catch(ApiException e){
            assertEquals("Brand and Category already exists in database",e.getMessage());
        }
    }

    @Test
    public void testSadUpdateBrandCategoryIfIdNotExists() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        BrandForm brand = new BrandForm();
        brand.setBrand("Addidas");
        brand.setCategory("sport");

        BrandData brandData = brandDto.addBrandCategory(brand);
        Integer id = brandData.getId();

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
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Addidas");
        brandForm.setCategory("sport");

        BrandData brandPojo = brandDto.addBrandCategory(brandForm);

        BrandData brandData=brandDto.getBrandCategoryById(brandPojo.getId());

        assertEquals(brandData.getBrand(), brandForm.getBrand());
        assertEquals(brandData.getCategory(), brandForm.getCategory());
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