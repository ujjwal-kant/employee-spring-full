package com.increff.employee.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.increff.employee.dao.InventoryDao;
import com.increff.employee.dao.OrderDao;
import com.increff.employee.dao.ProductDao;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;

@Service
public class OrderService {

    @Autowired
    private OrderDao orderdao;
    @Autowired
    private ProductDao productdao;
    @Autowired
    private InventoryDao inventorydao;

    public void add(List<OrderItemForm> orderItemforms) throws ApiException {
        for(OrderItemForm orderitem: orderItemforms)
        {
            OrderItemPojo p=convert(orderitem);
            orderdao.insert(p);
        }
    }

	public Boolean checkquantity(List<OrderItemForm> orderItemforms) {
        for(OrderItemForm orderitem: orderItemforms)
        {
            int QuantityPresentInInventory=inventorydao.selectByBarcode(orderitem.getBarcode()).getQuantity();
            // System.out.println(QuantityPresentInInventory+" "+orderitem.getQuantity())
            if(QuantityPresentInInventory<orderitem.getQuantity())
            return false;
        }
        return true;
	}

	public void updateInventory(List<OrderItemForm> orderItemforms) {
        for(OrderItemForm orderitem: orderItemforms)
        {
            int QuantityPresentInInventory=inventorydao.selectByBarcode(orderitem.getBarcode()).getQuantity();
            inventorydao.selectByBarcode(orderitem.getBarcode()).setQuantity(QuantityPresentInInventory-orderitem.getQuantity());
        }
	}

    public void undochangetoInventory(int id) {
    }

    public  OrderItemData convert(OrderItemPojo p) throws ApiException {
		OrderItemData d = new OrderItemData();
        d.setOrder_id(p.getOrderId());
		d.setId(p.getId());
        d.setQuantity(p.getQuantity());
        d.setMrp(p.getSellingPrice());
        d.setBarcode(productdao.GetBarcodeFromProductID(p));
		return d;
	}

	private  OrderItemPojo convert(OrderItemForm f) throws ApiException {
		OrderItemPojo p = new OrderItemPojo();
        p.setQuantity(f.getQuantity());
        p.setSellingPrice(f.getMrp());
        p.setProductId(productdao.GetProductIDFromBarcode(f));
        return p;
	}

    public List<OrderPojo> getAll() {
        return orderdao.getAll();
    }

    public OrderItemData convertOrderItemPojoToOrderItemData(OrderItemPojo orderitempojo, String barcode) {
        OrderItemData d = new OrderItemData();
        d.setOrder_id(orderitempojo.getOrderId());
		d.setId(orderitempojo.getId());
        d.setQuantity(orderitempojo.getQuantity());
        d.setMrp(orderitempojo.getSellingPrice());
        d.setBarcode(barcode);
        d.setProduct_id(orderitempojo.getProductId());
		return d;
    }

    public OrderData convertOrderPojoToOrderData(OrderPojo pojo, List<OrderItemData> orderItemData) {
        OrderData d=new OrderData();
        d.setId(pojo.getId());
        d.setCreatedAt(pojo.getDatetime());
        d.setOrders(orderItemData);
        d.setTotalAmount(0);
        return d;
    }

    public OrderPojo getById(int id) {
        return orderdao.getById(id);
    }

   
    
}
