package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ConversionUtil;
import com.increff.employee.util.NormaliseUtil;
import com.increff.employee.util.ValidateUtil;

@Component
public class InventoryDto {

    @Autowired
    private InventoryService inventoryService;
    
    @Autowired
    private ProductService productService;

    public List<InventoryData> getAllInventory() throws ApiException {
        List<InventoryPojo> list = inventoryService.selectAll();
        List<InventoryData> list2 = new ArrayList<InventoryData>();
        for (InventoryPojo inventoryPojo : list) {
            ProductPojo productPojo = productService.getIfExists(inventoryPojo.getProductId());
            list2.add(ConversionUtil.getInventoryData(inventoryPojo, productPojo .getName(), productPojo .getBarcode()));
        }
        return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public InventoryData updateInventory(InventoryForm inventoryForm) throws ApiException {
        NormaliseUtil.normalizeInventory(inventoryForm);
        ValidateUtil.validateInventoryForm(inventoryForm);
        
        ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = ConversionUtil.getInventoryPojo(inventoryForm, productPojo.getId());
		Integer productId=productPojo.getId();
		InventoryPojo old = inventoryService.selectByProductId(productId);
        old.setQuantity(inventoryPojo.getQuantity());
        inventoryService.update(old);
		return ConversionUtil.getInventoryData(old,productPojo.getName(), productPojo.getBarcode());
    }


    @Transactional(rollbackOn = ApiException.class)
    public InventoryData addOnInventory(InventoryForm inventoryForm) throws ApiException {
        NormaliseUtil.normalizeInventory(inventoryForm);
        ValidateUtil.validateInventoryForm(inventoryForm);   

        ProductPojo productPojo = productService.getByBarcode(inventoryForm.getBarcode());
        InventoryPojo inventoryPojo = inventoryService.getByProductId(productPojo.getId());
		Integer productId=productPojo.getId();
		InventoryPojo old = inventoryService.selectByProductId(productId);
        old.setQuantity(old.getQuantity()+inventoryForm.getQuantity());
        inventoryService.update(old);
		return ConversionUtil.getInventoryData(old,productPojo.getName(), productPojo.getBarcode());
    }

    public InventoryData getInventoryByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = productService.getByBarcode(barcode);
        InventoryPojo inventoryPojo = inventoryService.selectByProductId(productPojo.getId());
        return ConversionUtil.getInventoryData(inventoryPojo,productPojo.getName(), productPojo.getBarcode());
    }
    
}
