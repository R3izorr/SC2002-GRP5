package utils;

import java.util.regex.Pattern;

public class InputValidator { 
    private static final Pattern NRIC_PATTERN = Pattern.compile("^[ST]\\d{7}[A-Z]$");
    public static boolean isValidNRIC(String nric) {
        return NRIC_PATTERN.matcher(nric).matches();
    }
}