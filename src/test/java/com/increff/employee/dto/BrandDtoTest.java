package com.increff.employee.dto; 

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;


public class BrandDtoTest extends AbstractUnitTest {

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private BrandService brandService;

    // @Test
    // public void testNormalize() {
	// 	BrandForm p = new BrandForm();
    //     p.setBrand("Nike");
    //     p.setCategory("  spOrt");
	// 	brandDto.normailze(p);
    //     assertEquals("nike", p.getBrand());
    //     assertEquals("sport", p.getCategory());
	// }

    @Test
    public void testAddBrandCategory() throws ApiException{
		BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);

        BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");

        System.out.println(brandPojo.getBrand());

        // assertEquals(brandPojo.getId(), brandForm.getId());
        assertEquals(brandPojo.getBrand(), brandForm.getBrand());
        assertEquals(brandPojo.getCategory(), brandForm.getCategory());
    }

    // @Test
    // public void testUpdateBrandCategory() throws ApiException{
    //     BrandForm brandForm = new BrandForm();
    //     brandForm.setBrand("Nike");
    //     brandForm.setCategory("  spOrt");

    //     BrandForm brand = new BrandForm();
    //     brand.setBrand("Addidas");
    //     brand.setCategory("sport");

    //     BrandPojo b=brandService.addBrandCategory(brand);
    //     Integer id=b.getId();

    //     BrandPojo p=brandDto.updateBrandCategory(id,brandForm);

    //     assertEquals(brandForm.getBrand(), p.getBrand());
    //     assertEquals(brandForm.getCategory(), p.getCategory());
    // }

    // @Test 
    // public void testgetBrandCategorybyId() throws ApiException{
    //     BrandForm brand = new BrandForm();
    //     brand.setBrand("Addidas");
    //     brand.setCategory("sport");

    //     BrandPojo b=brandService.addBrandCategory(brand);
    //     Integer Id=b.getId();

    //     BrandData brandData=brandDto.getBrandCategoryById(Id);

    //     assertEquals(brandData.getBrand(), brand.getBrand());
    //     assertEquals(brandData.getCategory(), brand.getCategory());
    // }

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

//    @Test
//    void testsearchByBrandCategory(){
//        
//    }

}