package com.increff.employee.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.dao.BrandDao;
import com.increff.employee.model.data.InventoryData;
import com.increff.employee.model.form.InventoryForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.AbstractUnitTest;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private BrandDao brandDao;

    protected InventoryForm DummyInventoryForm(String Barcode,Integer Quantity){
        InventoryForm InventoryForm = new InventoryForm();
        InventoryForm.setBarcode(Barcode);
        InventoryForm.setQuantity(Quantity);
        return InventoryForm;
    }

    @Before
    public void setUp() throws ApiException{
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("brand");
        brandPojo.setCategory("category");
        brandDao.insert(brandPojo);

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
    public void testgetAllInventory() throws ApiException{
        List<InventoryData>listOfAllInventoryData = inventoryDto.getAllInventory();
        assertEquals(2,listOfAllInventoryData.size());
        
    }

    @Test
    public void testHappyUpadateInventory() throws ApiException{
        InventoryForm inventoryForm =new InventoryForm();

        String barcode="barcode1";
        Integer quantity=20;

        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);

        InventoryData inventoryData = inventoryDto.updateInventory(inventoryForm);
        InventoryData updateInventoryData = inventoryDto.getInventoryByBarcode(barcode);

        assertEquals(inventoryData.getBarcode(),updateInventoryData.getBarcode());
        assertEquals(inventoryData.getQuantity(),updateInventoryData.getQuantity());
    }

    @Test
    public void testSadUpdateInventory(){
        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", (Integer)null);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Quantity should not be empty.",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", -9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Quantity cannot be negative",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("BarCode should not be empty.",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("dfg", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Product with barcode dfg does not exists",e.getMessage());
        }

        Integer id=20;
        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Product with ID " + id + " does not exists",e.getMessage());
        }
    }

    @Test 
    public void testHappyAddOnInventory() throws ApiException{
        InventoryForm inventoryForm =new InventoryForm();

        String barcode="barcode2";
        Integer quantity=20;

        inventoryForm.setBarcode(barcode);
        inventoryForm.setQuantity(quantity);

        InventoryData inventoryData = inventoryDto.addOnInventory(inventoryForm);
        InventoryData updateInventoryData = inventoryDto.getInventoryByBarcode(barcode);

        assertEquals(inventoryData.getBarcode(),updateInventoryData.getBarcode());
        assertEquals(inventoryData.getQuantity(),updateInventoryData.getQuantity());
    }

    @Test
    public void testSadAddOnInventory() throws ApiException{
        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", (Integer)null);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Quantity should not be empty.",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", -9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Quantity cannot be negative",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("BarCode should not be empty.",e.getMessage());
        }

        try{
            InventoryForm inventoryForm = DummyInventoryForm("dfg", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Product with barcode dfg does not exists",e.getMessage());
        }

        Integer id=20;
        try{
            InventoryForm inventoryForm = DummyInventoryForm("barcode1", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Product with ID " + id + " does not exists",e.getMessage());
        }
    }

    @Test 
    public void testHappygetInventoryByBarcode() throws ApiException{
        InventoryData checkData = inventoryDto.updateInventory(DummyInventoryForm("barcode1",34));

        InventoryData inventoryData = inventoryDto.getInventoryByBarcode("barcode1");

        assertEquals(inventoryData.getBarcode(),checkData.getBarcode());
        assertEquals(inventoryData.getQuantity(),checkData.getQuantity());
    }

    @Test 
    public void testSadgetInventoryByBarcode() throws ApiException{
        try{
            inventoryDto.getInventoryByBarcode("gfdk");
        }
        catch(ApiException e){
            // BrandPojo brandPojo=brandService.getIfNameAndCategoryExists("nike","sport");
            assertEquals("Product with barcode gfdk does not exists",e.getMessage());
        }
    }
}
