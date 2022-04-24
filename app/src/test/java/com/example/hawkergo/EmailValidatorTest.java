package com.example.hawkergo;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.hawkergo.utils.TextValidatorHelper;

import org.junit.Test;

/**
 * Code was done with reference to the following medium post
 *          https://medium.com/mindorks/learn-unit-testing-in-android-by-building-a-sample-application-23ec2f6340e8
 *
 * */

public class EmailValidatorTest {
    @Test
    public void emailValidator_CorrectEmailSimple_ReturnsTrue() {
        assertTrue(TextValidatorHelper.isValidEmail("name@email.com"));
    }
    @Test
    public void emailValidator_CorrectEmailSubDomain_ReturnsTrue() {
        assertTrue(TextValidatorHelper.isValidEmail("name@email.co.uk"));
    }
    @Test
    public void emailValidator_InvalidEmailNoTld_ReturnsFalse() {
        assertFalse(TextValidatorHelper.isValidEmail("name@email"));
    }
    @Test
    public void emailValidator_InvalidEmailDoubleDot_ReturnsFalse() {
        assertFalse(TextValidatorHelper.isValidEmail("name@email..com"));
    }
    @Test
    public void emailValidator_InvalidEmailNoUsername_ReturnsFalse() {
        assertFalse(TextValidatorHelper.isValidEmail("@email.com"));
    }
    @Test
    public void emailValidator_EmptyString_ReturnsFalse() {
        assertFalse(TextValidatorHelper.isValidEmail(""));
    }
}
