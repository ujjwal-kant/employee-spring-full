package com.increff.employee.dto;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;

public class OrderDtoTest {
    
    @Autowired
    private OrderDto orderDto;

    @Autowired
    private BrandService brandService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Before
    public void addProduct() throws ApiException {
        BrandPojo brandPojo = new BrandPojo();
        brandPojo.setBrand("some brand");
        brandPojo.setCategory("some category");
        brandService.addBrandCategory(brandPojo);

        ProductPojo productPojo = new ProductPojo();
        productPojo.setName("some product");
        productPojo.setBarcode("abc123");
        productPojo.setMrp(2500.00);
        productPojo.setBrandId(brandCategoryPojo.getId());
        productService.addProduct(productPojo);
        
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(20);
        inventoryPojo.setProductId(productPojo.getId());
        inventoryService.addInventory(inventoryPojo);

        ProductPojo productPojo2 = new ProductPojo();
        productPojo2.setName("some product");
        productPojo2.setBarcode("abc234");
        productPojo2.setMrp(2000.00);
        productPojo2.setBrandId(brandCategoryPojo.getId());
//        productPojo.setBrandId(brandCategoryService.getByBrandCategory("some brand","some category").getId());
        productService.addProduct(productPojo2);
        InventoryPojo inventoryPojo2 = new InventoryPojo();
        inventoryPojo2.setQuantity(0);
        inventoryPojo2.setProductId(productPojo2.getId());
        inventoryService.addInventory(inventoryPojo2);
        //inventory quantity: 0
    }


}

