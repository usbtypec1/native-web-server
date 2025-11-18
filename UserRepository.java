import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class UserRepository {

  public UserRepository() throws IOException {
    if (!Files.exists(Resources.USERS_DIR)) {
      Files.createDirectories(Resources.USERS_DIR);
    }
  }

  public void createUser(User user) throws UserAlreadyExistsException, IOException {
    Path userFile = buildUserFilePath(user.getUsername());
    if (Files.exists(userFile)) {
      throw new UserAlreadyExistsException(user.getUsername());
    }
    writeUserToFile(userFile, user);
  }

  public boolean existsByUsername(String username) throws IOException {
    Path userFile = buildUserFilePath(username);
    return Files.exists(userFile);
  }

  public User getUserByUsername(String username) throws UserNotFoundException, IOException {
    Path userFile = resolveUserFilePath(username);
    List<String> lines = Files.readAllLines(userFile, StandardCharsets.UTF_8);
    try {
      String passwordHash = lines.get(0);
      return new User(username, passwordHash);
    } catch (IndexOutOfBoundsException e) {
      return null;
    }
  }

  private Path buildUserFilePath(String username) {
    if (username == null) {
      throw new IllegalArgumentException("Username is null");
    }
    return Resources.USERS_DIR.resolve(username + ".txt");
  }

  private Path resolveUserFilePath(String username) {
    Path userFile = buildUserFilePath(username);
    if (!Files.exists(userFile)) {
      throw new UserNotFoundException("User file does not exists: " + userFile.toString());
    }
    return userFile;
  }

  private void writeUserToFile(Path userFile, User user) throws IOException {
    String content = user.getPasswordHash() + "\n";
    Files.write(userFile, content.getBytes(StandardCharsets.UTF_8));
  }
}
