import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class PostRepository {
  public PostRepository() throws IOException {
    if (!Files.exists(Resources.POSTS_FILE)) {
      Files.write(
          Resources.POSTS_FILE,
          Collections.singletonList("id,login,title,content,createdAt"),
          StandardOpenOption.CREATE);
    }
  }

  public void createPost(Post post) throws IOException {
    String row = post.getId() + "," +
        post.getUsername() + "," +
        escape(post.getTitle()) + "," +
        escape(post.getContent()) + "," +
        post.getCreatedAt().toString();

    Files.write(
        Resources.POSTS_FILE,
        Collections.singletonList(row),
        StandardOpenOption.APPEND);
  }

  public Post getPostById(UUID postId) throws PostNotFoundException, IOException {
    List<String> lines;
    lines = Files.readAllLines(Resources.POSTS_FILE, StandardCharsets.UTF_8);

    for (int i = 1; i < lines.size(); i++) {
      Post post = parsePost(lines.get(i));
      if (post.getId().equals(postId)) {
        return post;
      }
    }

    throw new PostNotFoundException(postId);
  }

  public void deletePost(UUID postId) throws IOException {
    List<String> lines = Files.readAllLines(Resources.POSTS_FILE, StandardCharsets.UTF_8);

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

    Files.write(Resources.POSTS_FILE, updated, StandardCharsets.UTF_8);
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

  public Post[] getAllPosts() throws IOException {
    List<String> lines;
    lines = Files.readAllLines(Resources.POSTS_FILE, StandardCharsets.UTF_8);

    List<Post> result = new ArrayList<>();

    for (int i = 1; i < lines.size(); i++) {
      result.add(parsePost(lines.get(i)));
    }

    Post[] posts = result.toArray(new Post[0]);
    reverseArray(posts);
    return posts;
  }

  private static void reverseArray(Post[] posts) {
    for (int i = 0; i < posts.length / 2; i++) {
      Post temp = posts[i];
      posts[i] = posts[posts.length - 1 - i];
      posts[posts.length - 1 - i] = temp;
    }
  }
}
