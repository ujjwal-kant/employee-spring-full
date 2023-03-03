package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.dao.BrandDao;
import com.increff.pos.dto.InventoryDto;
import com.increff.pos.model.data.InventoryData;
import com.increff.pos.model.form.InventoryForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

public class InventoryDtoTest extends AbstractUnitTest{
    
    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryDto inventoryDto;

    @Autowired
    private BrandDao brandDao;

    protected InventoryForm InventoryForm(String barcode,Integer quantity){
        InventoryForm InventoryForm = new InventoryForm();
        InventoryForm.setBarcode(barcode);
        InventoryForm.setQuantity(quantity);
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
    public void testSadUpdateInventoryIfQuantityEmpty() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", (Integer)null);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Quantity should not be empty.",e.getMessage());
        }
    }

    @Test
    public void testSadUpdateInventoryIfQuantityIsNegative() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", -9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Quantity cannot be negative.",e.getMessage());
        }
    }
    
    @Test
    public void testSadUpdateInventoryIfBarcodeIsEmpty() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Barcode should not be empty.",e.getMessage());
        }

    }

    @Test
    public void testSadUpdateInventoryIfBarcodeNotExists() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("dfg", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Product with barcode dfg does not exists",e.getMessage());
        }
    }

    @Test
    public void testSadUpdateInventoryIdNotExists() throws ApiException{
        Integer id=20;
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", 9);
            inventoryDto.updateInventory(inventoryForm);
        }
        catch(ApiException e){
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
    public void testSadAddOnInventoryIfQuantityIsEmpty() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", (Integer)null);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Quantity should not be empty.",e.getMessage());
        }
    }

    @Test
    public void testSadAddOnInventoryIfQuantityIsNegative() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", -9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Quantity cannot be negative.",e.getMessage());
        }
    }

    @Test
    public void testSadAddOnInventoryIfBarcodeIsEmpty() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Barcode should not be empty.",e.getMessage());
        }
    }

    @Test
    public void testSadAddOnInventoryIfBarcodeNotExists() throws ApiException{
        try{
            InventoryForm inventoryForm = InventoryForm("dfg", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Product with barcode dfg does not exists",e.getMessage());
        }
    }

    @Test
    public void testSadAddOnInventoryIfproductIdNotExists() throws ApiException{
        Integer id=20;
        try{
            InventoryForm inventoryForm = InventoryForm("barcode1", 9);
            inventoryDto.addOnInventory(inventoryForm);
        }
        catch(ApiException e){
            assertEquals("Product with ID " + id + " does not exists",e.getMessage());
        }
    }

    @Test 
    public void testHappygetInventoryByBarcode() throws ApiException{
        InventoryData checkData = inventoryDto.updateInventory(InventoryForm("barcode1",34));

        InventoryData inventoryData = inventoryDto.getInventoryByBarcode("barcode1");

        assertEquals(inventoryData.getBarcode(),checkData.getBarcode());
        assertEquals(inventoryData.getQuantity(),checkData.getQuantity());
    }

    @Test 
    public void testSadGetInventoryByBarcode() throws ApiException{
        try{
            inventoryDto.getInventoryByBarcode("gfdk");
        }
        catch(ApiException e){
            assertEquals("Product with barcode gfdk does not exists",e.getMessage());
        }
    }
}
