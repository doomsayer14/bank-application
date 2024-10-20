package com.example.bankingapplication.validation;

import com.example.bankingapplication.annotation.Phone;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneValidator implements ConstraintValidator<Phone, String> {
    /**
     * This regex checks for:
     * Optional international code (e.g., +1).
     * Optional separators like spaces, dashes, or periods.
     * Parentheses around the area code (optional).
     * Handles different formats like:
     * 123-456-7890
     * (123) 456-7890
     * 123 456 7890
     * +1 123 456 7890
     * 1234567890
     */
    private static final String PHONE_PATTERN = "^\\+?(\\d{1,3})?[-. ]?\\(?\\d{1,4}\\)?[-. ]?\\d{1,4}[-. ]?\\d{1,9}$\n";

    @Override
    public void initialize(Phone constraintAnnotation) {
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext constraintValidatorContext) {
        return validatePhone(phone);
    }

    private boolean validatePhone(String phone) {
        Pattern pattern = Pattern.compile(PHONE_PATTERN);
        Matcher matcher = pattern.matcher(phone);
        return matcher.matches();
    }
}
