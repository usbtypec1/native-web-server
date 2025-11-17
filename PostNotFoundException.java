import java.util.UUID;

public class PostNotFoundException extends RuntimeException {
  // Supress "has no definition of serialVersionUID warning"
  private static final long serialVersionUID = 1L;

  public PostNotFoundException(UUID id) {
    super("Post by id " + id.toString() + " was not found.");
  }
}
