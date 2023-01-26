package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.InventoryData;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.InventoryService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class InventoryApiController{

	@Autowired
	private InventoryService service;

	@ApiOperation(value = "Adds Inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.POST)
	public void add(@RequestBody InventoryForm form) throws ApiException {
		// InventoryPojo p = convert(form);

		if(service.BarcodeCheker(form))
		{
			InventoryForm p1=service.CheckIfBarcodeAlreadyExistsInInventory(form);
			InventoryPojo p = convert(p1);
			service.add(p);
		}
	}

	
	@ApiOperation(value = "Deletes Inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an Inventory by ID")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.GET)
	public InventoryData get(@PathVariable int id) throws ApiException {
		InventoryPojo p = service.get(id);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all inventory")
	@RequestMapping(path = "/api/inventory", method = RequestMethod.GET)
	public List<InventoryData> getAll() throws ApiException {
		List<InventoryPojo> list = service.getAll();
		List<InventoryData> list2 = new ArrayList<InventoryData>();
		for (InventoryPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an inventory")
	@RequestMapping(path = "/api/inventory/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody InventoryForm f) throws ApiException {
		InventoryPojo p = convert(f);
		service.update(id, p);
	}
	

	private InventoryData convert(InventoryPojo p) throws ApiException {
		InventoryData d = new InventoryData();
		d.setQuantity(p.getQuantity());
		d.setId(p.getId());
		d.setCost(service.getMrpfromBarcode(p));
		d.setBarcode(p.getBarcode());
		return d;
	}

	private InventoryPojo convert(InventoryForm f) throws ApiException {
		InventoryPojo p = new InventoryPojo();
		p.setQuantity(f.getQuantity());
		p.setBarcode(f.getBarcode());
		return p;
	}
	
}