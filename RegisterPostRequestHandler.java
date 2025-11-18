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

  private String getPageWithError(String username, String errorMessage) throws IOException {
    Map<String, String> variables = new HashMap<>();
    variables.put("errorMessage", errorMessage);
    variables.put("username", username);
    return HtmlRenderer.renderWithVariables("register.html", variables);
  }

  public HttpResponse getResponse(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String username = form.get("username");
    String password = form.get("password");
    String confirmPassword = form.get("password2");

    try {
      String usernameValidationError = InputValidator.validateUsername(username);
      if (usernameValidationError != null) {
        return HttpResponseFactory
            .createBadRequestResponse(getPageWithError(username, getErrorMessage(usernameValidationError)));
      }

      String passwordsValidationError = InputValidator.validatePasswords(password, confirmPassword);
      if (passwordsValidationError != null) {
        return HttpResponseFactory
            .createBadRequestResponse(getPageWithError(username, getErrorMessage(passwordsValidationError)));
      }

      UserRepository userRepository = new UserRepository();
      if (userRepository.existsByUsername(username)) {
        return HttpResponseFactory
            .createBadRequestResponse(getPageWithError(username, getErrorMessage("Username is already taken")));
      }

      SessionManager sessionManager = new SessionManager();
      String session = sessionManager.createSession(username);
      User newUser = new User(username, PasswordHasher.hash(password));
      try {
        userRepository.createUser(newUser);
      } catch (UserAlreadyExistsException e) {
        return HttpResponseFactory
            .createBadRequestResponse(getPageWithError(username, getErrorMessage(e.getMessage())));
      }

      HttpResponse response = HttpResponseFactory.createRedirectResponse("/");
      response.getHeaders().setSession(session, SessionManager.SESSION_TIMEOUT_SECONDS);
      return response;
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
