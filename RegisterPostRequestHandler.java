import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class RegisterPostRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {

    Map<String, String> form = request.getForm();
    String username = form.get("username");
    String password = form.get("password");
    String confirmPassword = form.get("password2");

    // Validate input
    if (username == null || password == null || confirmPassword == null) {
      String errorMessage = """
          <div class="mb-4" id="error-box">
            <span class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
              <i class="fa-solid fa-circle-exclamation"></i>
              All fields are required
            </span>
          </div>""";
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      String body = HtmlRenderer.renderWithVariables("register.html", variables);
      return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
    }

    if (!password.equals(confirmPassword)) {
      String errorMessage = """
          <div class="mb-4" id="error-box">
            <span class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
              <i class="fa-solid fa-circle-exclamation"></i>
              Passwords do not match
            </span>
          </div>""";
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      variables.put("username", username);
      String body = HtmlRenderer.renderWithVariables("register.html", variables);
      return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
    }

    UserRepository userRepository = new UserRepository();
    try {
      if (userRepository.existsByUsername(username)) {
        String errorMessage = """
            <div class="mb-4" id="error-box">
              <span class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
                <i class="fa-solid fa-circle-exclamation"></i>
                Username already taken
              </span>
            </div>""";
        Map<String, String> variables = new HashMap<>();
        variables.put("errorMessage", errorMessage);
        variables.put("username", username);
        String body = HtmlRenderer.renderWithVariables("register.html", variables);
        return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
      }
    } catch (Exception e) {
      return new HttpResponse(HttpResponseStatus.InternalServerError, null, "Server error");
    }

    // Hash the password securely
    String hashedPassword = PasswordHasher.hash(password);

    // Save user
    String session = SessionIdGenerator.generate(255);
    User newUser = new User(UUID.randomUUID(), username, hashedPassword, session);
    userRepository.createUser(newUser);

    // Auto-login after registration
    HttpResponse response = new RedirectResponse("/");
    String cookie = String.format("sessionId=%s; Path=/; HttpOnly; Max-Age=3600", session);
    response.getHeaders().setHeader("Set-Cookie", cookie);

    return response;
  }
}
