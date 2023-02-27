package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

// import org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.BrandForm;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.StringUtil;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired 
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Test
    public void testHappyAddProduct() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("  Nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("Name");

        productDto.add(productForm);

        ProductPojo p=productService.getByBarcode("barcode");
        
        assertEquals(productForm.getMrp(), p.getMrp());
        assertEquals(StringUtil.toLowerCase(productForm.getName()), p.getName());
    }

    @Test
    public void testSadAddProduct() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("");
            productForm.setBrand("nike");
            productForm.setCategory("sport");
            productForm.setMrp(12.10);
            productForm.setName("name");

            productDto.add(productForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("BarCode should not be empty.",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("pol");
            productForm.setBrand("");
            productForm.setCategory("sport");
            productForm.setMrp(12.10);
            productForm.setName("name");

            productDto.add(productForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Brand should not be empty.",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("pol");
            productForm.setBrand("gkkkt");
            productForm.setCategory("");
            productForm.setMrp(12.10);
            productForm.setName("name");

            productDto.add(productForm);
        }
        catch(ApiException e){

            assertEquals("Category should not be empty.",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("pol");
            productForm.setBrand("gkkkt");
            productForm.setCategory("sgis");
            productForm.setMrp(-5.0);
            productForm.setName("name");

            productDto.add(productForm);
        }
        catch(ApiException e){

            assertEquals("Mrp cannot be Negative",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("pol");
            productForm.setBrand("gkkkt");
            productForm.setCategory("sgis");
            productForm.setMrp(5.0);
            productForm.setName("");

            productDto.add(productForm);
        }
        catch(ApiException e){

            assertEquals("Name should not be empty.",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("pol");
            productForm.setBrand("gkkkt");
            productForm.setCategory("sgis");
            productForm.setMrp(5.0);
            productForm.setName("kl");

            productDto.add(productForm);
        }
        catch(ApiException e){

            assertEquals("Brand gkkkt with category sgis does not exists",e.getMessage());
        }

        try{
            ProductForm productForm=new ProductForm();
            productForm.setBarcode("barcode");
            productForm.setBrand("  Nike");
            productForm.setCategory("sport");
            productForm.setMrp(12.10);
            productForm.setName("Name");

            productDto.add(productForm);

            ProductForm productFormDummy=new ProductForm();
            productFormDummy.setBarcode("barcode");
            productFormDummy.setBrand("  Nike");
            productFormDummy.setCategory("sport");
            productFormDummy.setMrp(12.10);
            productFormDummy.setName("Name");

            productDto.add(productForm);
        }
        catch(ApiException e){

            assertEquals("Another Product with barcode barcode already exists",e.getMessage());
        }
    }

    @Test
    public void testHappyGetProductById() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductPojo p=productService.getByBarcode(productForm.getBarcode());
        ProductData productData=productDto.getProductByID(p.getId());

        assertEquals(productForm.getMrp(), productData.getMrp());
        assertEquals(productForm.getName(), productData.getName());
        assertEquals(productForm.getBarcode(), productData.getBarcode());
        assertEquals(productForm.getBrand(), productData.getBrand());
        assertEquals(productForm.getCategory(), productData.getCategory());
    }

    @Test
    public void testSadGetProductbyId() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductPojo p=productService.getByBarcode(productForm.getBarcode());

        Integer id=10;

        try{
            productDto.getProductByID(p.getId());
        }
        catch(ApiException e){

            assertEquals("Product with given ID does not exit, id: " + id,e.getMessage());
        }
    }



    @Test
    public void testgetAllProduct() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);
        
        ProductForm productForm1=new ProductForm();
        productForm1.setBarcode("barcode1");
        productForm1.setBrand("nike");
        productForm1.setCategory("sport");
        productForm1.setMrp(12.10);
        productForm1.setName("name1");

        productDto.add(productForm1);

        List<ProductData> productDataList = productDto.getAllProduct();
        assertEquals(2,productDataList.size());
    }

    @Test
    public void testHappyUpdateProduct() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductForm productUpdateForm=new ProductForm();
        productUpdateForm.setBarcode("barcode1");
        productUpdateForm.setBrand("nike");
        productUpdateForm.setCategory("sport");
        productUpdateForm.setMrp(121.10);
        productUpdateForm.setName("name1");

        ProductPojo productpojo = productService.getByBarcode(productForm.getBarcode());
        Integer id = productpojo.getId();
        productDto.update(id,productUpdateForm);

        assertEquals(productUpdateForm.getBarcode(), productpojo.getBarcode());
        assertEquals(productUpdateForm.getMrp(), productpojo.getMrp());
        assertEquals(productUpdateForm.getName(), productpojo.getName());
    }

    @Test
    public void testSadUpdateProduct() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductForm productUpdateForm=new ProductForm();
        productUpdateForm.setBarcode("barcode1");
        productUpdateForm.setBrand("nik");
        productUpdateForm.setCategory("sport");
        productUpdateForm.setMrp(121.10);
        productUpdateForm.setName("name1");

        try{
            brandService.getIfBrandAndCategoryExists(productUpdateForm.getBrand(),productUpdateForm.getCategory());
        }
        catch(ApiException e){
            assertEquals("Brand " + productUpdateForm.getBrand()+ " with category " + productUpdateForm.getCategory() + " does not exists",e.getMessage());
        }
    }    

    @Test
    public void testHappySearchByProductNameAndBarcode() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm.setBrand("Nike");
        brandForm.setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductForm productFormSearch=new ProductForm();
        productFormSearch.setBrand("ni");
        productFormSearch.setCategory("sport");


        List<ProductData> listOfProductData = productDto.searchByProductNameAndBarcode(productFormSearch);
        assertEquals(1,listOfProductData.size());
    }
}
