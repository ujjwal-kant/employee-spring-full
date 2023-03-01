package com.increff.pos.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.pos.dto.OrderDto;
import com.increff.pos.model.data.OrderData;
import com.increff.pos.model.data.OrderDetailData;
import com.increff.pos.model.form.OrderItemForm;
import com.increff.pos.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping("/api/orders")
public class OrderApiController{

    @Autowired
    private OrderDto orderdto;

    @ApiOperation(value="Create an Order")
    @RequestMapping(path="",method= RequestMethod.POST)
    public OrderDetailData add(@RequestBody List<OrderItemForm> OrderItemforms) throws ApiException{
        return orderdto.addOrder(OrderItemforms);
    }

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderdto.getAllOrders();
    }

    @ApiOperation(value = "Get order by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public OrderDetailData getOrderDetails(@PathVariable Integer id) throws ApiException {
        return orderdto.getOrderDetails(id);
    }

    @ApiOperation(value = "Update order by id")
    @RequestMapping(path = "/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable Integer id, @RequestBody List<OrderItemForm> orderItems) throws ApiException {
        orderdto.updateOrder(id, orderItems);
    }

    // @ApiOperation(value = "Gets all order items of an order")
    // @RequestMapping(path = "/{id}/items", method = RequestMethod.GET)
    // public List<OrderItemData> getOrderItems(@PathVariable Integer id) throws ApiException {
    //     return orderdto.getOrderItems(id);
    // }

    // @ApiOperation(value = "Generate an Invoice")
    // @RequestMapping(path = "/invoice/{id}", method = RequestMethod.GET)
    // public String generateInvoice(@PathVariable Integer id) throws ApiException {
    //     return orderdto.generateInvoice(id);
    // }

    
}