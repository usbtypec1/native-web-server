import java.util.UUID;

public class User {
  private UUID id;
  private String login;
  private String passwordHash;
  private String sessionId;

  public User(UUID id, String login, String passwordHash, String sessionId) {
    this.id = id;
    this.login = login;
    this.passwordHash = passwordHash;
    this.sessionId = sessionId;
  }

  public UUID getId() {
    return id;
  }

  public String getLogin() {
    return login;
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
