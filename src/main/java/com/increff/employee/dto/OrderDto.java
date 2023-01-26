package com.increff.employee.dto;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.pojo.OrderItemPojo;
import com.increff.employee.pojo.OrderPojo;
import com.increff.employee.pojo.ProductPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.OrderItemService;
import com.increff.employee.service.OrderService;
import com.increff.employee.service.ProductService;

@Component
public class OrderDto {

    @Autowired
    private OrderService orderservice;
    @Autowired
    private OrderItemService orderitemservice;
    @Autowired
    private ProductService productservice;

    @Transactional
    public void add(List<OrderItemForm> orderItemforms) throws ApiException {
        validate(orderItemforms);
        if(orderservice.checkquantity(orderItemforms))
        {
            orderservice.updateInventory(orderItemforms);
            orderservice.add(orderItemforms);
        }
        else
        {
            throw new ApiException("Quantity is not present in Inventory");
        }
    }


    private void validate(List<OrderItemForm> orderItemforms) throws ApiException {
        if (orderItemforms == null || orderItemforms.isEmpty()) {
            throw new ApiException("Order items cannot be empty");
        }
        for (OrderItemForm orderItem : orderItemforms) {
            if (orderItem.getQuantity()<=0) {
                throw new ApiException("Quantity cannot be less than or equal to 0");
            }
        }
    }


    public List<OrderData> getAllOrders() throws ApiException {
        // int totalAmount=0;
        List<OrderPojo> orderPojo = orderservice.getAll();
        // System.out.println(orderPojo);
        List<OrderData> orderdata=new ArrayList<OrderData>();
        for(OrderPojo Pojo:orderPojo)
        {
            List<OrderItemPojo> orderItemPojo=orderitemservice.get(Pojo);
            // System.out.println(orderItemPojo);
            List<OrderItemData> orderItemData=new ArrayList<OrderItemData>();

            for(OrderItemPojo orderitempojo:orderItemPojo)
            {
                ProductPojo p=productservice.get(orderitempojo.getProductId());
                String barcode=p.getBarcode();
                // System.out.println(barcode);
                orderItemData.add(orderservice.convertOrderItemPojoToOrderItemData(orderitempojo,barcode));
                // System.out.println(orderItemData);
                // orderItemData.add(orderservice.convert(orderitempojo));
                // System.out.println(orderservice.convert(orderitempojo));
            }
            OrderData order=orderservice.convertOrderPojoToOrderData(Pojo,orderItemData);
            orderdata.add(order);
        }
        // System.out.println(orderdata);
        return orderdata;
    }


    public OrderData getOrderDetails(int id) throws ApiException {
        OrderPojo Pojo=orderservice.getById(id);
        List<OrderItemPojo> orderItemPojo=orderitemservice.get(Pojo);
            // System.out.println(orderItemPojo);
        List<OrderItemData> orderItemData=new ArrayList<OrderItemData>();

        for(OrderItemPojo orderitempojo:orderItemPojo)
        {
            ProductPojo p=productservice.get(orderitempojo.getProductId());
            String barcode=p.getBarcode();
                // System.out.println(barcode);
            orderItemData.add(orderservice.convertOrderItemPojoToOrderItemData(orderitempojo,barcode));
                // System.out.println(orderItemData);
                // orderItemData.add(orderservice.convert(orderitempojo));
                // System.out.println(orderservice.convert(orderitempojo));
        }
        OrderData order=orderservice.convertOrderPojoToOrderData(Pojo,orderItemData);
        return order;
    }

    @Transactional
    public void updateOrder(int id, List<OrderItemForm> orderItems) throws ApiException {
        validate(orderItems);
        if(orderservice.checkquantity(orderItems))
        {
            orderservice.updateInventory(orderItems);
            orderservice.add(orderItems);
        }
        orderservice.undochangetoInventory(id);
        // List<OrderItemPojo> newOrderItems = new ArrayList<OrderItemPojo>();
        // for (OrderItemForm orderItem : orderItems) {
        //     ProductPojo product = productservice.getProductByBarcode(orderItem.getBarcode());
        //     if (product == null) {
        //         throw new ApiException("Product with barcode " + orderItem.getBarcode() + " not found");
        //     }
        //     OrderItemPojo orderItemPojo = ConvertUtil.convertOrderItemFormToOrderItemPojo(orderItem);
        //     orderItemPojo.setOrderID(orderId);
        //     orderItemPojo.setProductId(product.getId());
        //     newOrderItems.add(orderItemPojo);
        //     inventoryService.reduce(orderItem.getBarcode(), orderItemPojo.getProductId(), orderItemPojo.getQuantity());
        // }
        // orderItemService.deleteByOrderId(orderId);
        // orderItemService.insertMutiple(newOrderItems);
    }
}
