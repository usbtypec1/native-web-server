import java.time.Instant;
import java.util.UUID;

public class Post {
  private UUID id;
  private String username;
  private String title;
  private String content;
  private Instant createdAt;

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
