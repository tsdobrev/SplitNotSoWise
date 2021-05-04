package bg.sofia.uni.fmi.mjt.splitwise.utilities;

public class NameValidator {
    private static final String validUsernameRegex = "[-_.a-zA-Z0-9]+";
    public static final String DESCRIPTION = "Name must be at least one-character-long and "
            + "should contain only digits, letters and symbols like -_.";

    public static boolean isValid(String username) {
        if (username == null) {
            return false;
        }
        return username.matches(validUsernameRegex);
    }
}
