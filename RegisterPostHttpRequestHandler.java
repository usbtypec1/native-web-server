import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

public class RegisterPostHttpRequestHandler extends HttpRequestHandler {

  public String getResponseBody(HttpRequest request) {
    Map<String, String> parsedBody = request.getParsedBody();
    UserRepository userRepository = new UserRepository("users");

    String login = parsedBody.get("login");
    String password = parsedBody.get("password");
    String password2 = parsedBody.get("password2");

    if (!password.equals(password2)) {
      System.out.println("Passwords do not match");
    }

    try {
      String passwordHash = PasswordHasher.hash(password);
      User user = new User(login, passwordHash, SessionIdGenerator.generate(64));
      userRepository.saveNewUser(user);
    } catch (Exception e) {
      System.out.println("Error while hashing password");
    }

    String filePath = "templates/index.html";
    try {
      return Files.readString(Paths.get(filePath));
    } catch (IOException e) {
      e.printStackTrace();
      return "<h1>Error loading template</h1>";
    }
  }
}
