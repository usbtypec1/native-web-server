import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;

public class PostRepository {
  private final Path storageFile;

  public PostRepository() {
    storageFile = Paths.get("posts.txt");
    try {
      if (!Files.exists(storageFile)) {
        Files.write(storageFile, Collections.singletonList("id,userId,title,content,createdAt"), StandardOpenOption.CREATE);
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage file " + e);
    }
  }

  public void saveNewPost(UUID userId, String title, String content) {
    UUID id = UUID.randomUUID();
    Instant createdAt = Instant.now();

    String line = String.format("\"%s\",\"%s\",\"%s\",\"%s\",\"%s\"", id.toString(), userId.toString(), title, content, createdAt.toString());
    try {
    Files.write(storageFile, Collections.singletonList(line), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new RuntimeException("Failed to save new post: " + e);
    }
  }
}
