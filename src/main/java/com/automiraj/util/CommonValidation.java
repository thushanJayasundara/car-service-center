package com.automiraj.util;

public class CommonValidation {

    public static boolean stringNullValidation(String inputString) {
        return inputString == null || inputString.isEmpty();
    }

    public static boolean isvalidNic(String nic) {
        final String NIC1 = "^[0-9]{9}[VX]$";
        final String NIC2 = "^[0-9]{12}$";
        if (nic.matches(NIC1) || nic.matches(NIC2)) {
            return false;
        }
        return true;
    }

    public static boolean isValidMobile(String mobileNum) {
        final String MOBILE = "^07[0-9]{8}$";
        final String TELEPHONE = "^011[0-9]{7}$";
        if (mobileNum.matches(MOBILE)) {
            return false;
        } else if (mobileNum.matches(TELEPHONE)) {
            return false;
        }
        return true;
    }

    public static boolean isValidEmail(String email) {
        final String EMAIL = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+[.]+[A-Za-z]{2,6}$";
        if (email.matches(EMAIL)) {
            return false;
        }
        return true;
    }
}

