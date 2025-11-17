import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PostRepository {
  private final Path storageFile;

  public PostRepository() {
    storageFile = Paths.get("posts.csv");

    try {
      if (!Files.exists(storageFile)) {
        Files.write(
            storageFile,
            Collections.singletonList("id,login,title,content,createdAt"),
            StandardOpenOption.CREATE
        );
      }
    } catch (IOException e) {
      throw new RuntimeException("Failed to create storage file: " + e.getMessage(), e);
    }
  }

  public void createPost(Post post) throws StorageException {
    String row = post.getId() + "," +
        post.getUsername() + "," +
        escape(post.getTitle()) + "," +
        escape(post.getContent()) + "," +
        post.getCreatedAt().toString();

    try {
      Files.write(
          storageFile,
          Collections.singletonList(row),
          StandardOpenOption.APPEND
      );
    } catch (IOException e) {
      throw new StorageException("Failed to write post to file: " + e);
    }
  }

  public Post getPostById(UUID postId) throws PostNotFoundException, StorageException {
    List<String> lines;
    try {
      lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new StorageException("Failed to read posts file: " + e);
    }

    for (int i = 1; i < lines.size(); i++) {
      Post post = parsePost(lines.get(i));
      if (post.getId().equals(postId)) {
        return post;
      }
    }

    throw new PostNotFoundException(postId);
  }

  public Post[] getPostsByUsername(String username) {
    List<Post> result = new ArrayList<>();

    List<String> lines;
    try {
      lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read posts file", e);
    }

    for (int i = 1; i < lines.size(); i++) {
      Post post = parsePost(lines.get(i));
      if (post.getUsername().equals(username)) {
        result.add(post);
      }
    }

    return result.toArray(new Post[0]);
  }

  public void updatePost(Post updatedPost) {
    List<String> lines;
    try {
      lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read posts file", e);
    }

    boolean updated = false;

    for (int i = 1; i < lines.size(); i++) {
      Post oldPost = parsePost(lines.get(i));
      if (oldPost.getId().equals(updatedPost.getId())) {
        lines.set(i, toCsvRow(updatedPost));
        updated = true;
        break;
      }
    }

    if (!updated) {
      throw new PostNotFoundException(updatedPost.getId());
    }

    try {
      Files.write(storageFile, lines, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to update post file", e);
    }
  }

  public void deletePost(UUID postId) {
    List<String> lines;
    try {
      lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to read posts file", e);
    }

    boolean removed = false;
    List<String> updated = new ArrayList<>();
    updated.add(lines.get(0)); // header

    for (int i = 1; i < lines.size(); i++) {
      Post post = parsePost(lines.get(i));
      if (!post.getId().equals(postId)) {
        updated.add(lines.get(i));
      } else {
        removed = true;
      }
    }

    if (!removed) {
      throw new PostNotFoundException(postId);
    }

    try {
      Files.write(storageFile, updated, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete post", e);
    }
  }

  private Post parsePost(String line) {
    String[] parts = splitCsvLine(line);

    UUID id = UUID.fromString(parts[0]);
    String username = parts[1];
    String title = unescape(parts[2]);
    String content = unescape(parts[3]);
    Instant createdAt = Instant.parse(parts[4]);

    return new Post(id, username, title, content, createdAt);
  }

  private String toCsvRow(Post post) {
    return post.getId() + "," +
        post.getUsername() + "," +
        escape(post.getTitle()) + "," +
        escape(post.getContent()) + "," +
        post.getCreatedAt().toString();
  }

  private String escape(String s) {
    return s.replace(",", "\\,");
  }

  private String unescape(String s) {
    return s.replace("\\,", ",");
  }

  private String[] splitCsvLine(String line) {
    List<String> result = new ArrayList<>();
    StringBuilder sb = new StringBuilder();
    boolean escaping = false;

    for (int i = 0; i < line.length(); i++) {
      char c = line.charAt(i);

      if (escaping) {
        sb.append(c);
        escaping = false;
      } else if (c == '\\') {
        escaping = true;
      } else if (c == ',') {
        result.add(sb.toString());
        sb.setLength(0);
      } else {
        sb.append(c);
      }
    }

    result.add(sb.toString());
    return result.toArray(new String[0]);
  }

  public Post[] getPosts(int limit, int offset) throws StorageException {
    if (offset < 0 || limit < 0) {
      throw new IllegalArgumentException("Offset and limit must be >= 0");
    }

    List<String> lines;
    try {
      lines = Files.readAllLines(storageFile, StandardCharsets.UTF_8);
    } catch (IOException e) {
      throw new StorageException("Failed to read posts file: " + e);
    }

    List<Post> result = new ArrayList<>();
    int startRow = 1 + offset;

    for (int i = startRow; i < lines.size() && result.size() < limit; i++) {
      result.add(parsePost(lines.get(i)));
    }

    return result.toArray(new Post[0]);
  }
}
