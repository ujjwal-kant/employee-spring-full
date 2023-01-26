package com.increff.employee.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.InventoryForm;
import com.increff.employee.pojo.InventoryPojo;
import com.increff.employee.pojo.ProductPojo;


@Service
public class InventoryService {

	@Autowired
	private InventoryDao dao;

	@Autowired
	private ProductDao productdao;

	@Transactional(rollbackOn = ApiException.class)
	public void add(InventoryPojo p) throws ApiException {
//		normalize(p);
//		if(StringUtil.isEmpty(p.getQuantity())) {
//			throw new ApiException("name cannot be empty");
//		}
		dao.insert(p);
	}

	@Transactional
	public void delete(int id) {
		dao.delete(id);
	}

	@Transactional(rollbackOn = ApiException.class)
	public InventoryPojo get(int id) throws ApiException {
		return getCheck(id);
	}

	@Transactional
	public List<InventoryPojo> getAll() {
		return dao.selectAll();
	}

	@Transactional(rollbackOn  = ApiException.class)
	public void update(int id, InventoryPojo p) throws ApiException {
//		normalize(p);
		InventoryPojo ex = getCheck(id);
		ex.setQuantity(p.getQuantity());
		dao.update(ex);
	}

	@Transactional
	public InventoryPojo getCheck(int id) throws ApiException {
		InventoryPojo p = dao.select(id);
		if (p == null) {
			throw new ApiException("Employee with given ID does not exit, id: " + id);
		}
		return p;
	}

	@Transactional
    public double getMrpfromBarcode(InventoryPojo p) throws ApiException {
		ProductPojo d=productdao.selectByBarcode(p.getBarcode());

		if(d==null){
			throw new ApiException("BarCode doesnot exists(Inventory Service)");
		}
		return d.getMrp();
    }

	@Transactional
	public Boolean BarcodeCheker(InventoryForm f) throws ApiException {
		ProductPojo d=productdao.selectByBarcode(f.getBarcode());

		if(d==null){
			throw new ApiException("BarCode doesnot exists(Inventory Service)");
		}
		return true;
	}

    @Transactional
	public InventoryForm CheckIfBarcodeAlreadyExistsInInventory(InventoryForm f) throws ApiException {
		InventoryPojo d= dao.selectByBarcode(f.getBarcode());

		if(d==null){
			return f;
		}
		
		f.setQuantity(d.getQuantity()+f.getQuantity());
		delete(d.getId());
		return f;
	}

//	protected static void normalize(InventoryPojo p) {
//		p.setName(StringUtil.toLowerCase(p.getQuantity()));
//	}
}
