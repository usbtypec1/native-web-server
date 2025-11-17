public class StorageException extends RuntimeException {
  // Supress "has no definition of serialVersionUID warning"
  private static final long serialVersionUID = 1L;

  public StorageException(String message) {
    super(message);
  }
}
