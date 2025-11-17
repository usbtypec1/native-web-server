import java.util.HashMap;
import java.util.Map;

public class LoginPostRequestHandler implements HttpRequestHandler {
  public HttpResponse getResponse(HttpRequest request) {

    Map<String, String> form = request.getForm();
    String username = form.get("username");
    String password = form.get("password");

    if (username == null || password == null) {
      String errorMessage = """
                    <div class="mb-4" id="error-box">
            <span
              class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
              <i class="fa-solid fa-circle-exclamation"></i>
              Username or password is not provided
            </span>
          </div>""";
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      String body = HtmlRenderer.renderWithVariables("login.html", variables);
      return new HttpResponse(HttpResponseStatus.BadRequest, null, body);

    }

    UserRepository userRepository = new UserRepository();

    User user = null;
    try {
      user = userRepository.getUserByUsername(username);
    } catch (UserNotFoundException e) {
      String errorMessage = """
                    <div class="mb-4" id="error-box">
            <span
              class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
              <i class="fa-solid fa-circle-exclamation"></i>
              User not found
            </span>
          </div>""";
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      variables.put("username", username);
      String body = HtmlRenderer.renderWithVariables("login.html", variables);
      return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
    }

    boolean isPasswordCorrect = PasswordHasher.verify(password, user.getPasswordHash());

    if (!isPasswordCorrect) {
      String errorMessage = """
                    <div class="mb-4" id="error-box">
            <span
              class="block w-full bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2 animate-fade-in">
              <i class="fa-solid fa-circle-exclamation"></i>
              Invalid password
            </span>
          </div>""";
      Map<String, String> variables = new HashMap<>();
      variables.put("errorMessage", errorMessage);
      variables.put("username", username);
      String body = HtmlRenderer.renderWithVariables("login.html", variables);
      return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
    }

    HttpResponse response = new RedirectResponse("/");
    String cookie = String.format("sessionId=%s; Path=/; HttpOnly; Max-Age=3600", SessionIdGenerator.generate(255));
    response.getHeaders().setHeader("Set-Cookie", cookie);
    return response;
  }
}
