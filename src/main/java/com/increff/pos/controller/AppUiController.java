package com.increff.pos.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class AppUiController extends AbstractUiController {

	@RequestMapping(value = "/ui/home")
	public ModelAndView home() {
		return mav("home.html");
	}

	@RequestMapping(value = "/ui/employee")
	public ModelAndView employee() {
		return mav("employee.html");
	}

	@RequestMapping(value = "/ui/admin")
	public ModelAndView admin() {
		return mav("user.html");
	}
	
	@RequestMapping(value = "/ui/brand")
	public ModelAndView brand() {
		return mav("brandCategory.html");
	}
	
	@RequestMapping(value = "/ui/product")
	public ModelAndView product() {
		return mav("product.html");
	}
	
	@RequestMapping(value = "/ui/inventory")
	public ModelAndView inventory() {
		return mav("inventory.html");
	}

	@RequestMapping(value = "/ui/order")
	public ModelAndView order() {
		return mav("order.html");
	}

	@RequestMapping(value = "/ui/inventoryreport")
	public ModelAndView inventoryReport() {
		return mav("inventoryreport.html");
	}

	@RequestMapping(value = "/ui/salesreport")
	public ModelAndView salesReport() {
		return mav("salesreport.html");
	}

	@RequestMapping(value = "/ui/brandreport")
	public ModelAndView brandReport() {
		return mav("brandreport.html");
	}

	@RequestMapping(value = "/ui/orderitem")
	public ModelAndView cart() {
		return mav("orderitem.html");
	}

	@RequestMapping(value ="ui/dailysales")
	public ModelAndView dailySales(){
		return mav("dailysales.html");
	}

	@RequestMapping(value ="ui/dailyreport")
	public ModelAndView dailyReport(){
		return mav("dailysales.html");
	}

	@RequestMapping(value ="ui/signup")
	public ModelAndView Signup(){
		return mav("signup.html");
	}
}
