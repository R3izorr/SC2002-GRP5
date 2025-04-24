package utils;

public class InputValidator {
    private static final String NRIC_REGEX = "^[ST]\\d{7}[A-Z]$";

    public static boolean isValidNRIC(String nric) {
        return nric != null && nric.toUpperCase().matches(NRIC_REGEX);
    }
}
