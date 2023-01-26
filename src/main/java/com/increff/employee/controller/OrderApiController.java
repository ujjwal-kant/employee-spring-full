package com.increff.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.increff.employee.dto.OrderDto;
import com.increff.employee.model.OrderData;
import com.increff.employee.model.OrderItemForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
public class OrderApiController{

    @Autowired
    private OrderDto orderdto;

    @ApiOperation(value="Create an Order")
    @RequestMapping(path="/api/order",method= RequestMethod.POST)
    public void add(@RequestBody List<OrderItemForm> OrderItemforms) throws ApiException{
        //update inventory and check inventory
        orderdto.add(OrderItemforms);
    }

    @ApiOperation(value = "Get all orders")
    @RequestMapping(path = "/api/order", method = RequestMethod.GET)
    public List<OrderData> getAll() throws ApiException {
        return orderdto.getAllOrders();
    }

    @ApiOperation(value = "Get order by id")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.GET)
    public OrderData getOrderDetails(@PathVariable int id) throws ApiException {
        return orderdto.getOrderDetails(id);
    }

    @ApiOperation(value = "Update order by id")
    @RequestMapping(path = "/api/order/{id}", method = RequestMethod.PUT)
    public void update(@PathVariable int id, @RequestBody List<OrderItemForm> orderItems) throws ApiException {
        orderdto.updateOrder(id, orderItems);
    }

    
}