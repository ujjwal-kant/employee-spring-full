package com.increff.employee.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;

public class InventoryServiceTest extends AbstractUnitTest{
    
    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private InventoryDao inventoryDao;

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
        productPojo.setBrand_category_id(brandCategoryId);
        return productPojo;        
    }

    protected InventoryPojo DummyInventoryPojo(Integer productId,Integer Quantity){
        InventoryPojo InventoryPojo = new InventoryPojo();
        InventoryPojo.setProductId(productId);
        InventoryPojo.setQuantity(Quantity);
        return InventoryPojo;
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
        productPojo.setBrand_category_id(brandPojo.getId());
        productService.add(productPojo);
        
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.add(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("product2");
        productPojo2.setBarcode("barcode2");
        productPojo2.setMrp(2.98);
        productPojo2.setBrand_category_id(brandPojo.getId());
        productService.add(productPojo2);

        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(5);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.add(inventoryPojo2);
    }

    @Test
    public void testHappyAddInventory() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);
        InventoryPojo i=DummyInventoryPojo(p.getId(),10);
        inventoryService.add(i);

        InventoryPojo inventoryPojo = inventoryDao.selectByProductId(p.getId());

        assertEquals(i.getProductId(), inventoryPojo.getProductId());
        assertEquals(i.getQuantity(), inventoryPojo.getQuantity());

    }

    @Test
    public void testSadGetByProductId() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);
        InventoryPojo i=DummyInventoryPojo(p.getId(),10);
        inventoryService.add(i);

        try{
            inventoryService.getByProductId(20);
        }
        catch(ApiException e){
            assertEquals("Product with ID 20 does not exists", e.getMessage());
        }

    }

    @Test
    public void testHappySelectAll() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        ProductPojo p = DummyProductPojo("name3", "barcode3",b.getId(), 98.01);
        productService.add(p);
        InventoryPojo i=DummyInventoryPojo(p.getId(),10);
        inventoryService.add(i);

        List<InventoryPojo> list = inventoryService.selectAll();

        assertEquals(3, list.size());
    }

    @Test
    public void testHappyInitializeInventory() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        
        ProductPojo p = DummyProductPojo("name4", "barcode4",b.getId(), 98.01);
        productService.add(p);
        inventoryService.initialize(p.getId());

        InventoryPojo i = inventoryService.getByProductId(p.getId());
        Integer cnt=0;

        assertEquals(cnt,i.getQuantity());
        assertEquals(i.getProductId(), p.getId());

    }

    @Test
    public void testHappyReduceInventory() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        
        ProductPojo p = DummyProductPojo("name4", "barcode4",b.getId(), 98.01);
        productService.add(p);
        inventoryService.initialize(p.getId());

        InventoryPojo i = inventoryService.getByProductId(p.getId());
        i.setQuantity(20);

        inventoryService.reduce("barcode4",p.getId(),18);
        Integer cnt=2;

        InventoryPojo i1 = inventoryService.getByProductId(p.getId());

        assertEquals(cnt,i1.getQuantity());
        assertEquals(i1.getProductId(), p.getId());

    }

    @Test
    public void testSadReduceInventory() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        
        ProductPojo p = DummyProductPojo("name4", "barcode4",b.getId(), 98.01);
        productService.add(p);
        inventoryService.initialize(p.getId());

        InventoryPojo i = inventoryService.getByProductId(p.getId());
        i.setQuantity(20);

        try{
            inventoryService.reduce("barcode4",p.getId(),22);
        }
        catch(ApiException e){
            assertEquals("Not enough quantity available for product, barcode:barcode4", e.getMessage());
        }

    }

    @Test
    public void testHappyIncreaseInventory() throws ApiException{
        BrandPojo b = brandService.getIfBrandAndCategoryExists("brand", "category");
        
        ProductPojo p = DummyProductPojo("name4", "barcode4",b.getId(), 98.01);
        productService.add(p);
        inventoryService.initialize(p.getId());

        InventoryPojo i = inventoryService.getByProductId(p.getId());
        i.setQuantity(20);

        inventoryService.increase(p.getId(),18);
        Integer cnt=38;

        InventoryPojo i1 = inventoryService.getByProductId(p.getId());

        assertEquals(cnt,i1.getQuantity());
        assertEquals(i1.getProductId(), p.getId());

    }

    


}
