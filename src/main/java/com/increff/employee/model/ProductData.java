package com.increff.employee.model;

public class ProductData extends ProductForm {

	private int id;
	private int brand_category_id;

	public int getBrand_category_id()
	{
		return brand_category_id;
	}

	public void setBrand_category_id(int brand_category_id)
	{
		this.brand_category_id=brand_category_id;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

}
