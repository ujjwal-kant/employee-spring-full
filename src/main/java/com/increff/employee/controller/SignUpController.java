package com.increff.employee.controller;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.increff.employee.dto.AdminDto;
import com.increff.employee.model.InfoData;
import com.increff.employee.model.UserForm;
import com.increff.employee.service.ApiException;

import io.swagger.annotations.ApiOperation;

@Controller
public class SignUpController extends AbstractUiController{

	@Autowired
	private InfoData info;
    @Autowired
    private AdminDto dto;

    @ApiOperation(value = "Registers a user in the application")
	@RequestMapping(path = "/session/signup", method = RequestMethod.POST)
	public ModelAndView signUpUser(UserForm form) throws ApiException {
        // System.out.println(form);
		Properties prop=new Properties();
		try{
			prop.load(new FileInputStream("email.properties"));
		}
		catch(IOException e){
			System.out.println("Failed to load email properties File");
			e.printStackTrace();
		}

		if(prop.containsKey(form.getEmail()))
		{
			form.setRole("supervisor");
		}
		else{
			form.setRole("operator");
		}

		try {
			dto.add(form);
		} 
        catch (ApiException ex) {
			info.setMessage(ex.getMessage());
			return mav("signup.html");
		}
		info.setMessage("");
		return new ModelAndView("redirect:/site/login");
	}

}