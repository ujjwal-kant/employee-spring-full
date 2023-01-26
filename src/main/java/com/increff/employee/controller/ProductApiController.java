
package com.increff.employee.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.ProductService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class ProductApiController{

	@Autowired
	private ProductService service;

	@ApiOperation(value = "Adds a Product")
	@RequestMapping(path = "/api/product", method = RequestMethod.POST)
	public void add(@RequestBody ProductForm form) throws ApiException {
		ProductPojo p = convert(form);
		service.add(p);
	}

	
	@ApiOperation(value = "Deletes Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.DELETE)
	public void delete(@PathVariable int id) {
		service.delete(id);
	}

	@ApiOperation(value = "Gets an Product by ID")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.GET)
	public ProductData get(@PathVariable int id) throws ApiException {
		// System.out.println("1kj");
		ProductPojo p = service.get(id);
		// System.out.println(p);
		return convert(p);
	}

	@ApiOperation(value = "Gets list of all product")
	@RequestMapping(path = "/api/product", method = RequestMethod.GET)
	public List<ProductData> getAll() throws ApiException {
		List<ProductPojo> list = service.getAll();
		List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list) {
			list2.add(convert(p));
		}
		return list2;
	}

	@ApiOperation(value = "Updates an Product")
	@RequestMapping(path = "/api/product/{id}", method = RequestMethod.PUT)
	public void update(@PathVariable int id, @RequestBody ProductForm f) throws ApiException {
		
		ProductPojo p = convert(f);
		service.update(id, p);
	}
	

	private  ProductData convert(ProductPojo p) throws ApiException {
		ProductData d = new ProductData();
		d.setBrand(service.getBrandFromProductPojo(p));
		d.setCategory(service.getCategoryFromProductPojo(p));
		d.setId(p.getId());
		d.setBrand_category_id(p.getBrand_category());
		d.setName(p.getName());
		d.setMrp(p.getMrp());
		d.setBarcode(p.getBarcode());
		// System.out.println(d);
		return d;
	}

	private  ProductPojo convert(ProductForm f) throws ApiException {
		ProductPojo p = new ProductPojo();
		p.setBarcode(f.getBarcode());
		p.setBrand_category(service.getIdfrombrandANDcategory(f));
		p.setName(f.getName());
		p.setMrp(f.getMrp());
		return p;
	}
	
}