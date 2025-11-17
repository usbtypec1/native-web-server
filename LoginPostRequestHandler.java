import java.util.Map;

public class LoginPostRequestHandler extends RedirectHandler {

  public LoginPostRequestHandler() {
    super("/");
  }

  private String loginUser(String username, String password) {
    UserRepository repo = new UserRepository();
    User user = repo.getUserByUsername(username);
    if (user == null) {
      return null;
    }
    if (!PasswordHasher.verify(password, user.getPasswordHash())) {
      return null;
    }
    user.setSessionId(SessionIdGenerator.generate(64));
    repo.createUser(user);
    return user.getSessionId();
  }

  public Map<String, String> getResponseHeaders(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String sessionId = loginUser(form.get("username"), form.get("password"));

    if (sessionId == null) {
      setRoute("/login");
      return super.getResponseHeaders(request);
    }

    Map<String, String> headers = super.getResponseHeaders(request);
    String cookie = String.format("sessionId=%s; Path=/; HttpOnly; Max-Age=3600", sessionId);
    headers.put("Set-Cookie", cookie);
    return headers;
  }
}
