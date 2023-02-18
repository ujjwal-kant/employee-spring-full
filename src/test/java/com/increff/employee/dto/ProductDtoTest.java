package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

// import org.junit.Assert.assertEquals;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.model.BrandForm;
import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

public class ProductDtoTest extends AbstractUnitTest {

    @Autowired 
    private ProductDto productDto;

    @Autowired
    private BrandDto brandDto;

    @Autowired
    private ProductService productService;

    @Test
    public void testadd() throws ApiException{
        BrandForm brandForm = new BrandForm();
        brandForm .setBrand("Nike");
        brandForm .setCategory("  spOrt");

        brandDto.addBrandCategory(brandForm);
        
        ProductForm productForm=new ProductForm();
        productForm.setBarcode("barcode");
        productForm.setBrand("nike");
        productForm.setCategory("sport");
        productForm.setMrp(12.10);
        productForm.setName("name");

        productDto.add(productForm);

        ProductPojo p=productService.getByBarcode("barcode");
        
        assertEquals(productForm.getMrp(), p.getMrp());
        assertEquals(productForm.getName(), p.getName());

    }

    @Test
    public void testgetProductByID() throws ApiException{
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
    public void testupdateProduct(){
        
    }


    
}
