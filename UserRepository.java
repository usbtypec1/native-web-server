import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class UserRepository {
  private final Path storageDir;

  public UserRepository() {
    storageDir = Paths.get("users");
    try {
      if (!Files.exists(storageDir)) {
        Files.createDirectories(storageDir);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage directory", e);
    }
  }

  public void createUser(User user) {
    Path userFile = buildUserFilePath(user.getUsername());
    if (Files.exists(userFile)) {
      throw new IllegalStateException("User already exists: " + user.getUsername());
    }
    writeUserToFile(userFile, user);
  }

  public User getUserByUsername(String username) throws UserNotFoundException, StorageException {
    Path userFile = resolveUserFilePath(username);
    try {
      List<String> lines = Files.readAllLines(userFile, StandardCharsets.UTF_8);
      try {
        UUID id = UUID.fromString(lines.get(0));
        String passwordHash = lines.get(1);
        String sessionId = lines.get(2);
        return new User(id, username, passwordHash, sessionId);
      } catch (IndexOutOfBoundsException e) {
        throw new StorageException("Invalid user file format.");
      }
    } catch (IOException e) {
      throw new StorageException("Could not read user from file: " + userFile.toString());
    }
  }

  public void updateUser(User user) {
    Path userFile = resolveUserFilePath(user.getUsername());
    writeUserToFile(userFile, user);
  }

  private Path buildUserFilePath(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username is null");
    }
    return storageDir.resolve(username + ".txt");
  }

  private Path resolveUserFilePath(String username) {
    Path userFile = buildUserFilePath(username);
    if (!Files.exists(userFile)) {
      throw new UserNotFoundException("User file does not exists: " + userFile.toString());
    }
    return userFile;
  }

  private void writeUserToFile(Path userFile, User user) {
    String content = user.getId().toString() + "\n" +
        user.getPasswordHash() + "\n" +
        user.getSessionId() + "\n";
    try {
      Files.write(userFile, content.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException("Failed to write user file: " + userFile, e);
    }
  }
}
