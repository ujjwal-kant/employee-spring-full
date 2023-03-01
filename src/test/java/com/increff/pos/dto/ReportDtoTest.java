package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.dto.ReportDto;
import com.increff.pos.model.data.ReportInventoryData;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.InventoryPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;

public class ReportDtoTest extends AbstractUnitTest {
    
    @Autowired
    private ReportDto reportDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Before
    public void addProduct() throws ApiException {
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
    public void testHappyGetInventoryReport() throws ApiException{
        List<ReportInventoryData>list = reportDto.getInventoryReport();
        assertEquals(1, list.size());
    }

    @Test
    public void  testHappyGetDailySalesReport() throws ApiException{
        
    }
}
