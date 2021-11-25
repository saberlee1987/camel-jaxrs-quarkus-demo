package com.example.dto;

import lombok.Data;

@Data
public class PersonEntity {
    private Integer id;
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Integer age;
    private String email;
    private String mobile;
}
