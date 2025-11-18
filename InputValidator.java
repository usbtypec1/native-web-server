public class InputValidator {

  public static String validatePasswords(String password, String password2) {
    if (password == null || password.isEmpty()) {
      return "Password is required.";
    }
    if (password.length() < 8) {
      return "Password must be at least 8 characters long.";
    }
    if (!password.matches("^[A-Za-z0-9_]+$")) {
      return "Password may contain only English letters, digits, and underscores.";
    }
    if (!password.matches(".*[A-Za-z].*")) {
      return "Password must contain at least one letter.";
    }
    if (!password.matches(".*\\d.*")) {
      return "Password must contain at least one digit.";
    }
    if (!password.equals(password2)) {
      return "Passwords do not match.";
    }
    return null;
  }

  public static String validateUsername(String username) {
    if (username == null || username.isEmpty()) {
      return "Username is required.";
    }
    if (!username.matches("^[A-Za-z0-9_]{3,20}$")) {
      return "Username must be 3–20 characters and use only English letters, digits, and underscores.";
    }
    return null;
  }

  public static String validatePostTitle(String title) {
    if (title == null || title.isEmpty()) {
      return "Title is required.";
    }
    if (title.length() < 3) {
      return "Title must be at least 3 characters.";
    }
    if (title.length() > 150) {
      return "Title must not exceed 150 characters.";
    }
    return null;
  }

  public static String validatePostContent(String content) {
    if (content == null || content.isEmpty()) {
      return "Content is required.";
    }
    if (content.length() > 10000) {
      return "Content must not exceed 10000 characters.";
    }
    return null;
  }
}
