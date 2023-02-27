package com.increff.employee.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.employee.model.form.UserForm;
import com.increff.employee.pojo.UserPojo;
import com.increff.employee.service.ApiException;
import com.increff.employee.service.UserService;
import com.increff.employee.util.ConversionUtil;
import com.increff.employee.util.StringUtil;

@Component
public class AdminDto {

    @Autowired
    private UserService service;

    public void add(UserForm userForm) throws ApiException {
        // System.out.print(userForm.getEmail());
        validateForm(userForm);
        
        UserPojo user = ConversionUtil.getUserPojoFromForm(userForm);
        //if email is not present in file (operartor)
        service.add(user);
    }

    private void validateForm(UserForm userForm) throws ApiException {
        if (StringUtil.isEmpty(userForm.getEmail()))
            throw new ApiException("Email cannot be empty");
        if (StringUtil.isEmpty(userForm.getPassword()))
            throw new ApiException("Password cannot be empty!");
        if (StringUtil.isEmpty(userForm.getConfirmPassword()))
            throw new ApiException("Confirm password cannot be empty!");
            // System.out.print(userForm.getConfirmPassword());
            // System.out.print(userForm.getPassword());
        // if (userForm.getPassword() != userForm.getConfirmPassword())
        //     throw new ApiException("Password does not match!");
    }


    
}
