package com.increff.pos.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.protobuf.Api;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

public class ProductServiceTest extends AbstractUnitTest{

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    protected BrandPojo DummyBrandPojo(String Brand,String category){
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand(Brand);
        brandPojo.setCategory(category);
        return brandPojo;
    }

    protected ProductPojo DummyProductPojo(String name,String barcode,Integer brandCategoryId,Double mrp){
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(name);
        productPojo.setMrp(mrp);
        productPojo.setBarcode(barcode);
        productPojo.setBrandCategoryId(brandCategoryId);
        return productPojo;        
    }

    @Before
    public void addProduct() throws ApiException{
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandService.addBrandCategory(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName("product1");
        productPojo.setBarcode("barcode1");
        productPojo.setMrp(2.97);
        productPojo.setBrandCategoryId(brandPojo.getId());
        productService.add(productPojo);
        
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.add(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("product2");
        productPojo2.setBarcode("barcode2");
        productPojo2.setMrp(2.98);
        productPojo2.setBrandCategoryId(brandPojo.getId());
        productService.add(productPojo2);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(5);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.add(inventoryPojo2);
    }

    @Test
    public void testHappyAddProduct() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);

        ProductPojo p1 = productService.getByBarcode("barcode3");

        assertEquals(p.getBarcode(), p1.getBarcode());
        assertEquals(p.getBrandCategoryId(), p1.getBrandCategoryId());
        assertEquals(p.getMrp(), p1.getMrp());
        assertEquals(p.getName(), p1.getName());
    }

    @Test
    public void testHappyGetById() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);

        Integer Id=p.getId();

        ProductPojo p1 = productService.get(Id);

        assertEquals(p.getBarcode(), p1.getBarcode());
        assertEquals(p.getBrandCategoryId(), p1.getBrandCategoryId());
        assertEquals(p.getMrp(), p1.getMrp());
        assertEquals(p.getName(), p1.getName());
    }

    @Test
    public void testHappyGetAllProduct() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);

        List<ProductPojo> list = productService.getAllProduct();

        assertEquals(3, list.size());
    }

    @Test
    public void testHappyUpdateProduct() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);

        ProductPojo productPojo = DummyProductPojo("name4", "barcode3",b.getId(), 98.03);

        productService.update(p.getId(),productPojo);

        ProductPojo p1 = productService.get(p.getId());

        assertEquals(productPojo.getBarcode(), p1.getBarcode());
        assertEquals(p.getBrandCategoryId(), p1.getBrandCategoryId());
        assertEquals(productPojo.getMrp(), p1.getMrp());
        assertEquals(productPojo.getName(), p1.getName());

    }

    @Test
    public void testSadUpdateProduct() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);

        ProductPojo productPojo = DummyProductPojo("name4", "barcode3",b.getId(), 98.03);

        try{
            productService.update(20,productPojo);
        }
        catch(ApiException e){
            assertEquals("Product with given ID does not exist, id: 20" , e.getMessage());
        }

    }
    
}
