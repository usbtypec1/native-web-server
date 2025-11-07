import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class UserRepository {
  private final Path storageDir;

  public UserRepository(String storageDirPath) {
    this.storageDir = Paths.get(storageDirPath);
    try {
      if (!Files.exists(this.storageDir)) {
        Files.createDirectories(this.storageDir);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage directory", e);
    }
  }

  public void saveNewUser(User user) {
    Path userFile = storageDir.resolve(user.getLogin() + ".txt");
    if (Files.exists(userFile)) {
      throw new IllegalStateException("User already exists: " + user.getLogin());
    }
    writeUserToFile(userFile, user);
  }

  public User readUserByLogin(String login) {
    Path userFile = storageDir.resolve(login + ".txt");
    if (!Files.exists(userFile)) {
      return null;
    }
    try {
      List<String> lines = Files.readAllLines(userFile, StandardCharsets.UTF_8);
      String loginValue = null;
      String passwordHashValue = null;
      String sessionIdValue = null;

      for (String line : lines) {
        if (line.startsWith("login=")) {
          loginValue = line.substring("login=".length());
        } else if (line.startsWith("passwordHash=")) {
          passwordHashValue = line.substring("passwordHash=".length());
        } else if (line.startsWith("sessionId=")) {
          sessionIdValue = line.substring("sessionId=".length());
        }
      }

      if (loginValue == null || passwordHashValue == null) {
        throw new IOException("Corrupted user file: " + userFile);
      }

      return new User(loginValue, passwordHashValue, sessionIdValue);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read user file: " + userFile, e);
    }
  }

  public void updateUser(User user) {
    Path userFile = storageDir.resolve(user.getLogin() + ".txt");
    if (!Files.exists(userFile)) {
      throw new IllegalStateException("User not found: " + user.getLogin());
    }
    writeUserToFile(userFile, user);
  }

  private void writeUserToFile(Path userFile, User user) {
    String content =
    "login=" + user.getLogin() + "\n" +
    "passwordHash=" + user.getPasswordHash() + "\n" +
    "sessionId=" + user.sessionId() + "\n";
    try {
      Files.write(userFile, content.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException("Failed to write user file: " + userFile, e);
    }
  }
}
