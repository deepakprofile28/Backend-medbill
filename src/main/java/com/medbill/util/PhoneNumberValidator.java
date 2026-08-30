package com.medbill.util;

import java.util.HashMap;
import java.util.Map;

public class PhoneNumberValidator {

    private static final Map<String, Integer> COUNTRY_DIGITS =
            new HashMap<>();

    static {

        // India
        COUNTRY_DIGITS.put("+91", 10);

        // USA
        COUNTRY_DIGITS.put("+1", 10);

        // Canada
        COUNTRY_DIGITS.put("+1", 10);

        // UK
        COUNTRY_DIGITS.put("+44", 10);

        // UAE
        COUNTRY_DIGITS.put("+971", 9);

        // Singapore
        COUNTRY_DIGITS.put("+65", 8);

        // Australia
        COUNTRY_DIGITS.put("+61", 9);

        // New Zealand
        COUNTRY_DIGITS.put("+64", 8);

        // Germany
        COUNTRY_DIGITS.put("+49", 10);

        // France
        COUNTRY_DIGITS.put("+33", 9);

        // Japan
        COUNTRY_DIGITS.put("+81", 10);

        // China
        COUNTRY_DIGITS.put("+86", 11);

        // Malaysia
        COUNTRY_DIGITS.put("+60", 9);

        // South Africa
        COUNTRY_DIGITS.put("+27", 9);

        // Saudi Arabia
        COUNTRY_DIGITS.put("+966", 9);

        // Qatar
        COUNTRY_DIGITS.put("+974", 8);

        // Kuwait
        COUNTRY_DIGITS.put("+965", 8);

        // Oman
        COUNTRY_DIGITS.put("+968", 8);
    }

    private PhoneNumberValidator() {
    }

    public static boolean isValid(
            String countryCode,
            String mobile) {

        if (countryCode == null ||
                mobile == null) {

            return false;
        }

        Integer requiredDigits =
                COUNTRY_DIGITS.get(countryCode);

        if (requiredDigits == null) {
            return false;
        }

        // Remove spaces, -, brackets etc.
        String cleanedMobile =
                mobile.replaceAll("\\D", "");

        return cleanedMobile.length()
                == requiredDigits;
    }

    public static int getRequiredDigits(
            String countryCode) {

        Integer digits =
                COUNTRY_DIGITS.get(countryCode);

        if (digits == null) {
            return 0;
        }

        return digits;
    }
}