package com.increff.pos.dto;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderDetailData;
import com.increff.pos.model.data.OrderItemData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.pojo.OrderItemPojo;
import com.increff.pos.pojo.OrderPojo;
import com.increff.pos.pojo.ProductPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.InventoryService;
import com.increff.pos.service.OrderItemService;
import com.increff.pos.service.OrderService;
import com.increff.pos.service.ProductService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormaliseUtil;
import com.increff.pos.util.ValidateUtil;

@Component
public class OrderDto {

    @Autowired
    private OrderService orderService;

    @Autowired
    private ProductService productService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private OrderItemService orderItemService;

    @Transactional(rollbackOn = ApiException.class)
    public OrderDetailData addOrder(List<OrderItemForm> orderItemForms) throws ApiException {
        NormaliseUtil.normalizeOrderItem(orderItemForms);
        ValidateUtil.validateOrderForm(orderItemForms);
        
        checkForRedundancyInOrderItemForms(orderItemForms);
        OrderPojo orderPojo = orderService.createNewOrder();
        List<OrderItemPojo> orderItemPojoList = new ArrayList<OrderItemPojo>();
        List<OrderItemData> orderItemDataList = new ArrayList<OrderItemData>();
        updateInventory(orderPojo.getId(), orderItemForms, orderItemPojoList, orderItemDataList);
        OrderData orderData = ConversionUtil.getOrderData(orderPojo, orderItemPojoList);
        OrderDetailData orderDetailData = ConversionUtil.getOrderDetailsData(orderData, orderItemDataList);
        return orderDetailData;
    }

    public void updateInventory(Integer orderId, List<OrderItemForm> orderItemFormList,List<OrderItemPojo> orderItemPojoList, List<OrderItemData> orderItemDataList) throws ApiException {
        for (OrderItemForm orderItemForm : orderItemFormList) {
            ProductPojo productPojo = productService.getByBarcode(orderItemForm.getBarcode());
            if (productPojo.getMrp() < orderItemForm.getSellingPrice()) {
                throw new ApiException("Selling Price higher than MRP(INR "+ productPojo.getMrp() + " ) for " + orderItemForm.getBarcode());
            }
            OrderItemPojo orderItemPojo = ConversionUtil.getOrderItemPojo(orderItemForm, orderId, productPojo.getId());
            OrderItemData orderItemData = ConversionUtil.getOrderItemData(orderItemPojo, productPojo);
            orderItemPojoList.add(orderItemPojo);
            orderItemDataList.add(orderItemData);
            orderItemService.insert(orderItemPojo);
            inventoryService.reduce(orderItemForm.getBarcode(), orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
    }

    @Transactional(rollbackOn = ApiException.class)
    public OrderDetailData getOrderDetails(int orderId) throws ApiException {
        OrderPojo orderPojo = orderService.getById(orderId);
        List<OrderItemPojo> orderItemPojo = orderItemService.get(orderId);
        OrderData orderData = ConversionUtil.getOrderData(orderPojo, orderItemPojo);
        List<OrderItemData> orderItemDatas = new ArrayList<OrderItemData>();
        for (OrderItemPojo orderItem : orderItemPojo) {
            ProductPojo product = productService.getIfExists(orderItem.getProductId());
            OrderItemData orderItemData = ConversionUtil.getOrderItemData(orderItem, product);
            orderItemDatas.add(orderItemData);
        }
        return ConversionUtil.getOrderDetailsData(orderData, orderItemDatas);
    }

    public List<OrderData> getAllOrders() throws ApiException {
        List<OrderPojo> orderPojos = orderService.getAll();
        List<OrderData> orderDatas = new ArrayList<OrderData>();
        for (OrderPojo orderPojo : orderPojos) {
            List<OrderItemPojo> orderItemPojos = orderItemService.get(orderPojo.getId());
            orderDatas.add(ConversionUtil.getOrderData(orderPojo, orderItemPojos));
        }
        return orderDatas;
    }

    @Transactional(rollbackOn = ApiException.class)
    public List<OrderItemData> updateOrder(Integer orderId, List<OrderItemForm> orderItemForms) throws ApiException {

        OrderPojo orderPojo = orderService.getById(orderId);
        // if(orderPojo.getIsInvoiceCreated())
        checkIfInvoiceIsAlreadyCreated(orderPojo);
        ValidateUtil.validateOrderForm(orderItemForms);
        NormaliseUtil.normalizeOrderItem(orderItemForms);
        revertInventory(orderId);
        orderItemService.deleteByOrderId(orderId);
        //To do check for multiple orders of similar kind (throw api exception on them)
        checkForRedundancyInOrderItemForms(orderItemForms);
        List<OrderItemPojo> orderItemPojolist = new ArrayList<>();
        List<OrderItemData> orderItemDataList = new ArrayList<>();
        updateInventory(orderPojo.getId(), orderItemForms, orderItemPojolist, orderItemDataList);
        // return orderItemPojolist;
        return orderItemDataList;
        

    }

    private void checkIfInvoiceIsAlreadyCreated(OrderPojo orderPojo) throws ApiException {
        if(orderPojo.getIsInvoiceCreated()){
            throw new ApiException("Cannot Edit an order whose invoice is already generated");
        }
    }

    public void revertInventory(int orderId) throws ApiException {
        List<OrderItemPojo> orderItemPojoList = orderItemService.get(orderId);
        for (OrderItemPojo orderItemPojo : orderItemPojoList) {
            inventoryService.increase(orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        }
    }   
    
    public void checkForRedundancyInOrderItemForms(List<OrderItemForm> orderItemForms) throws ApiException {
        Set<String> hash_Set = new HashSet<String>();
        for(OrderItemForm orderitem: orderItemForms){
            if(hash_Set.contains(orderitem.getBarcode())){
                throw new ApiException("Order items multiple instance of barcode "+ orderitem.getBarcode());
            }
            hash_Set.add(orderitem.getBarcode());
        }
    }
}
