package com.example.services.impl;

import com.example.services.InputValidator;
import javax.inject.Named;
import javax.inject.Singleton;

@Singleton
@Named(value = "validator")
public class InputValidatorImpl implements InputValidator {
    @Override
    public boolean validateNationalCode(String nationalCode) {

        if (nationalCode == null || nationalCode.trim().equals(""))
            return false;

        if (nationalCode.trim().length() < 10)
            return false;

        if (!nationalCode.trim().matches("\\d{10}"))
            return false;

        int k = 10;
        int sum = 0;
        int n;
        int controlNumber = Integer.parseInt(String.valueOf(nationalCode.charAt(nationalCode.length() - 1)));
        for (int i = 0; i < nationalCode.length() - 1; i++) {
            n = Integer.parseInt(String.valueOf(nationalCode.charAt(i))) * k;
            sum += n;
            k--;
        }
        int r = sum % 11;
        int checkControlNumber = 11 - r;
        if (r >= 2 && checkControlNumber == controlNumber) {
            return true;
        } else return r == controlNumber;
    }
}
