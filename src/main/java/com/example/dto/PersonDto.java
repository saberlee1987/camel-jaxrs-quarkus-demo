package com.example.dto;

import com.example.annotations.NationalCode;
import lombok.Data;

import javax.validation.constraints.*;
import java.io.Serializable;
@Data
public class PersonDto implements Serializable {
    @NotBlank(message = "firstName is Required")
    private String firstName;
    @NotBlank(message = "lastName is Required")
    private String lastName;
    @NotBlank(message = "nationalCode is Required")
    @Size(min = 10,max = 10,message = "nationalCode must be 10 digits")
    @Pattern(regexp = "\\d+",message = "Please enter valid nationalCode")
    @NationalCode(message = "Please enter valid nationalCode")
    private String nationalCode;
    @NotNull(message = "age is Required")
    @Positive(message = "age must be > 0")
    @Max(value = 999,message = "age must be < 1000")
    private Integer age;
    @NotBlank(message = "email is Required")
    @Email(message = "Please enter valid email ",regexp = "^\\w+@[a-zA-Z_]+?\\.[a-zA-Z]{2,3}$")
    private String email;
    @NotBlank(message = "mobile is Required")
    @Size(max = 11,min = 11,message = "mobile must be 11 digits")
    @Pattern(regexp = "\\d+",message = "Please enter valid mobile")
    private String mobile;

}
