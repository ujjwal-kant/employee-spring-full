package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.pojo.InventoryPojo;


@Service
@Transactional(rollbackOn = ApiException.class)
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	public InventoryPojo add(InventoryPojo inventoryPojo) throws ApiException {
		dao.insert(inventoryPojo);
        return inventoryPojo;
	}
    
	public InventoryPojo getByProductId(Integer id) throws ApiException {
        InventoryPojo inventoryPojo = dao.selectByProductId(id);
        if(inventoryPojo == null) {
            throw new ApiException("Product with ID " + id + " does not exists");
        }
        return inventoryPojo;
    }

	public List<InventoryPojo> selectAll() throws ApiException {
		return dao.selectAll();
	}

    public void initialize(Integer productId) throws ApiException {
        InventoryPojo inventoryPojo = new InventoryPojo();
        inventoryPojo.setQuantity(0);
        inventoryPojo.setProductId(productId);
        dao.insert(inventoryPojo);
    }

    public void reduce(String barcode, Integer productId, Integer quantity) throws ApiException {
        InventoryPojo inventoryPojo = getByProductId(productId);
        if (inventoryPojo.getQuantity() < quantity) {
            throw new ApiException("Not enough quantity available for product, barcode:" + barcode);
        }
        inventoryPojo.setQuantity(inventoryPojo.getQuantity() - quantity);
        dao.update(inventoryPojo);
    }

    public void increase(Integer productId, Integer quantity) throws ApiException {
        InventoryPojo p = getByProductId(productId);
        p.setQuantity(p.getQuantity() + quantity);
        dao.update(p);
    }

    // @Transactional
    // public InventoryPojo selectByProductId(Integer id){
    //     return dao.selectByProductId(id);
    // }

    public InventoryPojo update(InventoryPojo old) {
        dao.update(old);
        return old;
    }
}
