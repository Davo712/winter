package com.winter.context.generatedClasses;

import lombok.Data;

import java.util.UUID;

@Data
public class User {
    private Integer id;
    private String username;
    private Boolean active;
    private String activation_code;
    public User() {

    }
}
