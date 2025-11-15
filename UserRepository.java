import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.ArrayList;
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

  public void saveNewUser(User user) {
    Path userFile = storageDir.resolve(user.getLogin() + ".txt");
    if (Files.exists(userFile)) {
      throw new IllegalStateException("User already exists: " + user.getLogin());
    }
    writeUserToFile(userFile, user);
  }

  public User readUserByUsername(String username) {
    Path userFile = storageDir.resolve(username + ".txt");
    if (!Files.exists(userFile)) {
      return null;
    }
    try {
      List<String> lines = Files.readAllLines(userFile, StandardCharsets.UTF_8);
      UUID id = UUID.fromString(lines.get(0));
      String passwordHash = lines.get(1);
      String sessionId = lines.get(2);

      List<Post> posts = new ArrayList<>();
      for (String line : lines.subList(3, lines.size())) {
        String[] postLine = line.split("\",\"");
        UUID postId = UUID.fromString(postLine[0]);
        String postTitle = postLine[1];
        String postContent = postLine[2];
        Instant postCreatedAt = Instant.parse(postLine[3]);
        posts.add(new Post(postId, postTitle, postContent, postCreatedAt));
      }

      return new User(id, username, passwordHash, sessionId);
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
    "id=" + user.getId().toString() + "\n" +
    "passwordHash=" + user.getPasswordHash() + "\n" +
    "sessionId=" + user.getSessionId() + "\n";
    try {
      Files.write(userFile, content.getBytes(StandardCharsets.UTF_8));
    } catch (IOException e) {
      throw new RuntimeException("Failed to write user file: " + userFile, e);
    }
  }
}
