import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class LoginPostRequestHandler implements HttpRequestHandler {
  private String getErrorMessage(String message) {
    return String.format(
        """
              <div class="mb-4" id="error-box">
              <span
                class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
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

    Map<String, String> variables = new HashMap<>();

    try {
      if (username == null || password == null) {
        String errorMessage = getErrorMessage("Username or password is not provided");
        variables.put("errorMessage", errorMessage);
        String body = HtmlRenderer.renderWithVariables("login.html", variables);
        return HttpResponseFactory.createBadRequestResponse(body);
      }

      UserRepository userRepository = new UserRepository();
      User user = null;
      try {
        user = userRepository.getUserByUsername(username);
      } catch (UserNotFoundException e) {
        String errorMessage = getErrorMessage("User not found");
        variables.put("errorMessage", errorMessage);
        variables.put("username", username);
        String body = HtmlRenderer.renderWithVariables("login.html", variables);
        return HttpResponseFactory.createBadRequestResponse(body);
      }

      boolean isPasswordCorrect = PasswordHasher.verify(password, user.getPasswordHash());

      if (!isPasswordCorrect) {
        String errorMessage = getErrorMessage("Invalid password");
        variables.put("errorMessage", errorMessage);
        variables.put("username", username);
        String body = HtmlRenderer.renderWithVariables("login.html", variables);
        return HttpResponseFactory.createBadRequestResponse(body);
      }

      SessionManager sessionManager = new SessionManager();
      HttpResponse response = HttpResponseFactory.createRedirectResponse("/");
      String session = sessionManager.createSession(username);
      response.getHeaders().setSession(session, SessionManager.SESSION_TIMEOUT_SECONDS);
      return response;
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
