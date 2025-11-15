public class User {
  private String login;
  private String passwordHash;
  private String sessionId;

  public User(String login, String passwordHash, String sessionId) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.sessionId = sessionId;
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
