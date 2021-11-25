package com.example.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class PersonDto implements Serializable {
    private String firstName;
    private String lastName;
    private String nationalCode;
    private Integer age;
    private String email;
    private String mobile;
}
