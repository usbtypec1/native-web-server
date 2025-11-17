import java.util.UUID;

public class User {
  private UUID id;
  private String username;
  private String passwordHash;
  private String sessionId;

  public User(UUID id, String username, String passwordHash, String sessionId) {
    this.id = id;
    this.username = username;
    this.passwordHash = passwordHash;
    this.sessionId = sessionId;
  }

  public UUID getId() {
    return id;
  }

  public String getUsername() {
    return username;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public String getSessionId() {
    return sessionId;
  }

  public void setSessionId(String sessionId) {
    this.sessionId = sessionId;
  }
}
