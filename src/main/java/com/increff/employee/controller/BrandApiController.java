package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.BrandDto;
import com.increff.employee.model.BrandData;
import com.increff.employee.model.BrandForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/brand")
public class BrandApiController{

	@Autowired
	private BrandDto branddto;

	@ApiOperation(value = "Adds a Brand and Category")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public BrandPojo addBrandCategory(@RequestBody BrandForm form) throws ApiException {
		return branddto.addBrandCategory(form);
	}

	@ApiOperation(value = "Gets a Brand by ID")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public BrandData getBrandCategory(@PathVariable Integer id) throws ApiException {
		return branddto.getBrandCategoryById(id);
	}

	@ApiOperation(value = "Gets list of all Brand")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<BrandData> getAllBrandCategory() {
		return branddto.getAllBrandCategory();
	}

	@ApiOperation(value = "Updates a Brand")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public BrandPojo updateBrandCategory(@PathVariable Integer id, @RequestBody BrandForm form) throws ApiException {
		 return branddto.updateBrandCategory(id,form);
	}

	@ApiOperation(value = "Search by Brand and Category")
	@RequestMapping(path = "/search", method = RequestMethod.POST)
	public List<BrandData> searchBrandCategory(@RequestBody BrandForm form) throws ApiException {
		return branddto.searchByBrandCategory(form);
	}
	
}