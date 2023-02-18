package com.increff.employee.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserForm {

    private String email;
    private String password;
    private String confirmPassword;
    private String role;
}
