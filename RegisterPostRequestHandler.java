import java.util.Map;
import java.util.UUID;

public class RegisterPostRequestHandler extends RedirectHandler {

  public RegisterPostRequestHandler() {
    super("/");
  }

  private String registerUser(String username, String password, String password2) {
    if (!password.equals(password2)) {
      System.out.print("Passwords do not match");
      return null;
    }
    if (username == null) {
      System.out.print("Username is not specified");
      return null;
    }
    if (password == null || password2 == null) {
      System.out.print("Passwords do not specified");
      return null;
    }
    UserRepository repo = new UserRepository();
    String passwordHash = PasswordHasher.hash(password);
    if (passwordHash == null) {
      System.out.print("Invalid hash");
      return null;
    }
    User user = new User(UUID.randomUUID(), username, passwordHash, SessionIdGenerator.generate(64));
    repo.createUser(user);
    return user.getSessionId();
  }

  public Map<String, String> getResponseHeaders(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String sessionId = registerUser(form.get("username"), form.get("password"), form.get("password2"));

    if (sessionId == null) {
      setRoute("/register");
      return super.getResponseHeaders(request);
    }

    Map<String, String> headers = super.getResponseHeaders(request);
    String cookie = String.format("sessionId=%s; Path=/; HttpOnly; Max-Age=3600", sessionId);
    headers.put("Set-Cookie", cookie);
    return headers;
  }
}
