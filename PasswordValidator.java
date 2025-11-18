public class PasswordValidator {

    public static String validate(String password) {
        if (password == null || password.isEmpty()) {
            return "Password cannot be empty";
        }

        StringBuilder helper = new StringBuilder();

        if (password.length() < 8) {
            helper.append("- Password should be at least 8 characters long\n");
        }

        if (!password.matches(".*[A-Z].*")) {
            helper.append("- Password should contain at least one uppercase letter\n");
        }

        if (!password.matches(".*[a-z].*")) {
            helper.append("- Password should contain at least one lowercase letter\n");
        }

        if (!password.matches(".*\\d.*")) {
            helper.append("- Password should contain at least one digit\n");
        }

        if (helper.length() == 0) {
            return "Password is empty";
        }

        return helper.toString().trim();
    }
}
