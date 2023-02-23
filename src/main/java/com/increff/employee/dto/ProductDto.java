package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.ProductData;
import com.increff.employee.model.ProductForm;
import com.increff.employee.pojo.BrandPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.BrandService;
import com.increff.employee.service.InventoryService;
import com.increff.employee.service.ProductService;
import com.increff.employee.util.ConversionUtil;
import com.increff.employee.util.HelperUtil;
import com.increff.employee.util.NormaliseUtil;
import com.increff.employee.util.ValidateUtil;

@Component
public class ProductDto {

    @Autowired
    private ProductService productService;

    @Autowired
    private BrandService brandService;

    @Autowired
    private InventoryService inventoryService;

    @Transactional(rollbackOn = ApiException.class)
    public void add(ProductForm form) throws ApiException {
        NormaliseUtil.normalizeProduct(form);    
        ValidateUtil.validateProductForm(form);

        BrandPojo brandPojo = brandService.getIfBrandAndCategoryExists(form.getBrand(), form.getCategory());
        ProductPojo productPojo = ConversionUtil.getProductPojo(form, brandPojo.getId());
        productService.checkIfBarcodeExists(productPojo.getBarcode());
        productService.add(productPojo);
        inventoryService.initialize(productPojo.getId());
    }

    @Transactional
    public ProductData getProductByID(Integer id) throws ApiException {
		ProductPojo productPojo=productService.getCheck(id);
        BrandPojo brandPojo = brandService.get(productPojo.getBrand_category_id());
        return ConversionUtil.getProductData(productPojo, brandPojo.getBrand(), brandPojo.getCategory());
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<ProductData> getAllProduct() throws ApiException {
        List<ProductPojo>list1= productService.getAllProduct();
        List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list1) {
            BrandPojo b = brandService.get(p.getBrand_category_id());
            list2.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));
		}
		return list2;
    }

    @Transactional(rollbackOn = ApiException.class)
    public void update(Integer id, ProductForm form) throws ApiException {
        ValidateUtil.validateProductForm(form);
        NormaliseUtil.normalizeProduct(form);
        BrandPojo brandPojo = brandService.getIfBrandAndCategoryExists(form.getBrand(), form.getCategory());
        ProductPojo productPojo = ConversionUtil.getProductPojo(form, brandPojo.getId());
        productService.update(id,productPojo);
    }

    @Transactional
    public List<ProductData> searchByProductNameAndBarcode(ProductForm form) throws ApiException {
        HelperUtil.setProductForm(form);
        List<ProductPojo> list1 = productService.serachByProductNameAndBarcode(form.getBarcode(),form.getName());
        List<ProductData> list2 = new ArrayList<ProductData>();
		for (ProductPojo p : list1) {
            BrandPojo b = brandService.get(p.getBrand_category_id());
            list2.add(ConversionUtil.getProductData(p, b.getBrand(), b.getCategory()));

		}
		return list2;
    }
    
}
