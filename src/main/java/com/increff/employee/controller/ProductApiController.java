
package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.ProductDto;
import com.increff.employee.model.data.ProductData;
import com.increff.employee.model.form.ProductForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/products")
public class ProductApiController{

	@Autowired
	private ProductDto productDto;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		productDto.add(form);
	}

	@ApiOperation(value = "Gets a Product by ID")
	@RequestMapping(path = "/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		return productDto.getProductByID(id);
	}

	@ApiOperation(value = "Gets list of all product")
	@RequestMapping(path = "", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		return productDto.getAllProduct();
	}

	@ApiOperation(value = "Updates an Product")
	@RequestMapping(path = "/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable Integer id, @RequestBody ProductForm form) throws ApiException {
		productDto.update(id,form);
	}	

	@ApiOperation(value = "Search by  Product Name or Barcode")
	@RequestMapping(path = "/search", method = RequestMethod.POST)
	public List<ProductData> search(@RequestBody ProductForm form) throws ApiException {
		return productDto.searchByProductNameAndBarcode(form);
	}
}