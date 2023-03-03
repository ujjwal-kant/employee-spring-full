package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.ProductData;
import com.increff.pos.model.form.ProductForm;
import com.increff.pos.pojo.BrandPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.BrandService;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.HelperUtil;
import com.increff.pos.util.NormaliseUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public ProductData add(ProductForm form) throws ApiException {
        NormaliseUtil.normalizeProduct(form);    
        ValidateUtil.validateProductForm(form);

        BrandPojo brandPojo = brandService.getIfBrandAndCategoryExists(form.getBrand(), form.getCategory());
        ProductPojo productPojo = ConversionUtil.getProductPojo(form, brandPojo.getId());
        productService.checkIfBarcodeExists(productPojo.getBarcode());
        productService.add(productPojo);
        inventoryService.initialize(productPojo.getId());
        return ConversionUtil.getProductData(productPojo,form.getBrand(),form.getCategory());
    }

    public ProductData getProductByID(Integer id) throws ApiException {
		ProductPojo productPojo=productService.getIfExists(id);
        BrandPojo brandPojo = brandService.getBrandCategorybyID(productPojo.getBrandCategoryId());
        return ConversionUtil.getProductData(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    public List<ProductData> getAllProduct() throws ApiException {
        List<ProductPojo>list1= productService.getAllProduct();
        List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list1) {
            BrandPojo b = brandService.getBrandCategorybyID(p.getBrandCategoryId());
            list2.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));
		}
		return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public ProductData update(Integer id, ProductForm form) throws ApiException {
        NormaliseUtil.normalizeProduct(form);
        ValidateUtil.validateProductForm(form);
       
        BrandPojo brandPojo = brandService.getIfBrandAndCategoryExists(form.getBrand(), form.getCategory());
        ProductPojo productPojo = ConversionUtil.getProductPojo(form, brandPojo.getId());
        productService.update(id,productPojo);
        return ConversionUtil.getProductData(productPojo,form.getBrand(),form.getCategory());
    }

    public List<ProductData> searchByProductNameAndBarcode(ProductForm form) throws ApiException {
        HelperUtil.setProductForm(form);
        List<ProductPojo> list1 = productService.searchByProductNameAndBarcode(form.getBarcode(),form.getName());
        List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list1) {
            BrandPojo b = brandService.getBrandCategorybyID(p.getBrandCategoryId());
            list2.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));

		}
		return list2;
    }
}