public class UserNotFoundException extends RuntimeException {
  // Supress "has no definition of serialVersionUID warning"
  private static final long serialVersionUID = 1L;

  public UserNotFoundException(String username) {
    super("User by username " + username + " was not found.");
  }
}
