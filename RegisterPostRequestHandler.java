import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterPostRequestHandler implements HttpRequestHandler {

  private String getErrorMessage(String message) {
    return String.format(
        """
            <div class="mb-4" id="error-box">
              <span class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
                <i class="fa-solid fa-circle-exclamation"></i>
                %s
              </span>
            </div>""",
        message);
  }

  public HttpResponse getResponse(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String username = form.get("username");
    String password = form.get("password");
    String confirmPassword = form.get("password2");

    if (username == null || password == null || confirmPassword == null) {
      String errorMessage = getErrorMessage("All fields are required");
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      String body = HtmlRenderer.renderWithVariables("register.html", variables);
      return HttpResponseFactory.createBadRequestResponse(body);
    }

    if (!password.equals(confirmPassword)) {
      String errorMessage = getErrorMessage("Passwords do not match");
      Map<String, String> variables = Map.of("errorMessage", errorMessage, "username", username);
      String body = HtmlRenderer.renderWithVariables("register.html", variables);
      return HttpResponseFactory.createBadRequestResponse(body);
    }

    UserRepository userRepository = new UserRepository();
    try {
      if (userRepository.existsByUsername(username)) {
        String errorMessage = getErrorMessage("Username already taken");
        Map<String, String> variables = Map.of("errorMessage", errorMessage, "username", username);
        String body = HtmlRenderer.renderWithVariables("register.html", variables);
        return HttpResponseFactory.createBadRequestResponse(body);
      }
    } catch (Exception e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }

    // Hash the password securely
    String hashedPassword = PasswordHasher.hash(password);

    try {
      SessionManager sessionManager = new SessionManager();
      String session = sessionManager.createSession(username);
      User newUser = new User(username, hashedPassword);
      userRepository.createUser(newUser);

      HttpResponse response = HttpResponseFactory.createRedirectResponse("/");
      String cookie = String.format("session=%s; Path=/; HttpOnly; Max-Age=%s", session,
          SessionManager.SESSION_TIMEOUT_SECONDS);
      response.getHeaders().setHeader("Set-Cookie", cookie);
      return response;
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
