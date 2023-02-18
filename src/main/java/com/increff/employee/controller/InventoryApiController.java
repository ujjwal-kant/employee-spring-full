package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.InventoryDto;
import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/inventory")
public class InventoryApiController{

	@Autowired
	private InventoryDto dto;

	@ApiOperation(value = "Gets list of all inventory")
	@RequestMapping(path = "", method = RequestMethod.GET)
    public List<InventoryData> getAllInventory() throws ApiException {
        return dto.getAllInventory();
    }

	@ApiOperation(value = "Updates an inventory")
    @RequestMapping(path = "", method = RequestMethod.PUT)
    public InventoryData updateInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        return dto.updateInventory(inventoryForm);
    }

    @ApiOperation(value = "Add on existing inventory")
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    public InventoryData addonInventory(@RequestBody InventoryForm inventoryForm) throws ApiException {
        return dto.addOnInventory(inventoryForm);
    }

    @ApiOperation(value = "Get Inventory By Barcode")
    @RequestMapping(path = "/{barcode}", method = RequestMethod.GET)
    public InventoryData getInventoryByBarcode(@PathVariable String barcode) throws ApiException {
        return dto.getInventoryByBarcode(barcode);
    }

    
}