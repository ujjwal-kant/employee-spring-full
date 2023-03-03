package com.increff.pos.dto;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.increff.pos.model.form.UserForm;
import com.increff.pos.pojo.UserPojo;
import com.increff.pos.service.AbstractUnitTest;
import com.increff.pos.service.ApiException;
import com.increff.pos.service.UserService;

public class AdminDtoTest extends AbstractUnitTest {
    
    @Autowired
    private UserDto adminDto;

    @Autowired 
    private UserService userService;

    @Test
    public void testHappyAddUser() throws ApiException{
        UserForm userForm = new UserForm();
        userForm.setEmail("ujjwal");
        userForm.setPassword("ujjwal@123");
        userForm.setRole("a");

        adminDto.add(userForm);

        UserPojo userPojo = userService.get("ujjwal");

        assertEquals(userForm.getEmail(), userPojo.getEmail());
        
    }

    @Test
    public void testSadAddUserIfUserAlreadyExists() throws ApiException{
        UserForm userForm = new UserForm();
        userForm.setEmail("ujjwal");
        userForm.setPassword("ujjwal@123");
        userForm.setRole("a");

        adminDto.add(userForm);

        UserForm userFormdulplicate = new UserForm();
        userFormdulplicate.setEmail("ujjwal");
        userFormdulplicate.setPassword("ujjwal@123");
        userFormdulplicate.setRole("a");

        try{
            adminDto.add(userFormdulplicate);
        }
        catch(ApiException e){
            assertEquals("User with given email already exists", e.getMessage());
        }
        
    }

    @Test
    public void testSadAddUserIfEmailIsEmpty() throws ApiException{
        UserForm userForm = new UserForm();
        userForm.setEmail("");
        userForm.setPassword("ujjwal@123");

        try{
            adminDto.add(userForm);
        }
        catch(ApiException e){
            assertEquals("Email cannot be empty", e.getMessage());
        }
    }

    @Test
    public void testHappyAddUserIfPasswordIsEmpty() throws ApiException{
        UserForm userForm = new UserForm();
        userForm.setEmail("ujjwal5");
        userForm.setPassword("");

        try{
            adminDto.add(userForm);
        }
        catch(ApiException e){
            assertEquals("Password cannot be empty!", e.getMessage());
        }
    }
}
