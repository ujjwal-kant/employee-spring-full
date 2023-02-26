package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.ProductDao;
import com.increff.employee.pojo.ProductPojo;

@Service
public class ProductService {

	@Autowired
	private ProductDao dao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(ProductPojo productPojo) throws ApiException {
		dao.insert(productPojo);
	}

	
	public ProductPojo get(Integer id) throws ApiException {
		return dao.select(id);
	}

	public List<ProductPojo> getAllProduct() throws ApiException {
		List<ProductPojo> list1=dao.selectAll();
		return list1;
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(Integer id, ProductPojo productPojo) throws ApiException {
		productPojo.setId(id);
		ProductPojo old = getIfExists(id);
        old.setName(productPojo.getName());
        old.setMrp(productPojo.getMrp());
		dao.update(old);
	}

	public ProductPojo getIfExists(Integer id) throws ApiException {
        ProductPojo b = dao.select(id);
        if (b == null) {
            throw new ApiException("Product with given ID does not exist, id: " + id);
        }
        return b;
    }


	// @Transactional
	// public ProductPojo getCheck(Integer id) throws ApiException {
	// 	ProductPojo p = dao.select(id);
	// 	if (p == null) {
	// 		throw new ApiException("Product with given ID does not exit, id: " + id);
	// 	}
	// 	return p;
	// }
	

	public ProductPojo checkIfBarcodeExists(String barcode) throws ApiException {
        ProductPojo productPojo = dao.selectByBarcode(barcode);
        if(productPojo != null) {
            throw new ApiException("Another Product with barcode " + productPojo.getBarcode() + " already exists");
        }
		return productPojo;
    }

	public ProductPojo getByBarcode(String barcode) throws ApiException {
        ProductPojo productPojo = dao.selectByBarcode(barcode);
        if(productPojo == null) {
            throw new ApiException("Product with barcode " + barcode + " does not exists");
        }
        return productPojo;
    }

    public List<ProductPojo> serachByProductNameAndBarcode(String barcode,String productName) throws ApiException {
        List<ProductPojo> list1=dao.serachByProductNameAndBarcode(productName,barcode);
		return list1;
    }
}