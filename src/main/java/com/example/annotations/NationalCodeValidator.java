package com.example.annotations;

import com.example.services.InputValidator;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Singleton
public class NationalCodeValidator implements ConstraintValidator<NationalCode, String> {
    @Inject
    InputValidator validator;

    @Override
    public boolean isValid(String nationalCode, ConstraintValidatorContext constraintValidatorContext) {
        return validator.validateNationalCode(nationalCode);
    }
}
