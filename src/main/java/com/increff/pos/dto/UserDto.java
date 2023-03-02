package com.increff.pos.dto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;
import com.increff.pos.util.ConversionUtil;
import com.increff.pos.util.NormaliseUtil;
import com.increff.pos.util.StringUtil;

@Component
public class UserDto {

    @Autowired
    private UserService service;

    public void add(UserForm userForm) throws ApiException {
        NormaliseUtil.normalizeUserForm(userForm);
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
    }
}
