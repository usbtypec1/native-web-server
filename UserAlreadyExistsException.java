public class UserAlreadyExistsException extends RuntimeException {
  // Supress "has no definition of serialVersionUID warning"
  private static final long serialVersionUID = 1L;

  public UserAlreadyExistsException(String username) {
    super("User by username " + username + " already exists.");
  }
}
