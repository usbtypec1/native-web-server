import java.time.Instant;
import java.util.UUID;

public class Post {
  private final UUID id;
  private final String username;
  private final String title;
  private final String content;
  private final Instant createdAt;

  public Post(UUID id, String username, String title, String content, Instant createdAt) {
    this.id = id;
    this.username = username;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getTitle() {
    return title;
  }

  public String getContent() {
    return content;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }
}
