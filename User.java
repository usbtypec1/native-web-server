public class User {
  private String login;
  private String passwordHash;
  private String sessionId;

  public User(String login, String passwordHash, String sessionId) {
    this.login = login;
    this.passwordHash = passwordHash;
    this.sessionId = sessionId;
  }

  String getLogin() {
    return login;
  }

  String getPasswordHash() {
    return passwordHash;
  }

  String sessionId() {
    return sessionId;
  }
}
