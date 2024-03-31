package dev.sbohdan.h2.utils;

import java.util.regex.Pattern;

public class StringPatternValidationUtils {
    private StringPatternValidationUtils() {
        throw new RuntimeException();
    }
    private static final String EMAIL_REGEX = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final Pattern EMAIL_PATTERN = Pattern.compile(EMAIL_REGEX);

    private static final String NAME_REGEX = "^([a-zA-Z]{2,}'?-?[a-zA-Z]{2,})";
    private static final Pattern NAME_PATTERN = Pattern.compile(NAME_REGEX);

    private static final String PASSWORD_REGEX = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,20}$";
    private static final Pattern PASSWORD_PATTERN = Pattern.compile(PASSWORD_REGEX);

    public static boolean matchesEmail(String email) {
        return matches(EMAIL_PATTERN, email);
    }

    public static boolean matchesName(String name) {
        return matches(NAME_PATTERN, name);
    }

    public static boolean matchesPassword(String password) {
        return matches(PASSWORD_PATTERN, password);
    }

    private static boolean matches(final Pattern pattern, final String testString) {
        return pattern.matcher(testString).matches();
    }
}
