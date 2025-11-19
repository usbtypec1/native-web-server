import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class CreatePostPostRequestHandler implements HttpRequestHandler {

  private String getErrorMessage(String message) {
    return String.format(
        """
            <div class="w-full max-w-xl mb-4">
              <span class="block bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2">
                <i class="fa-solid fa-circle-exclamation"></i> %s
              </span>
            </div>""",
        message);
  }

  public HttpResponse getResponse(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String title = form.get("title");
    String content = form.get("content");

    try {
      if (title == null || title.isBlank() || content == null || content.isBlank()) {
        Map<String, String> variables = new HashMap<>();
        variables.put("errorMessage", getErrorMessage("Title and content cannot be empty."));
        String body = HtmlRenderer.renderWithVariables("create-post.html", variables);
        return HttpResponseFactory.createBadRequestResponse(body);
      }

      String session = request.getHeaders().getSession();
      if (session == null) {
        return HttpResponseFactory.createRedirectToLoginResponse();
      }

      SessionManager sessionManager = new SessionManager();
      String username = sessionManager.getUsername(session);
      if (username == null) {
        return HttpResponseFactory.createRedirectToLoginResponse();
      }

      PostRepository postRepository = new PostRepository();
      postRepository.createPost(username,
          URLDecoder.decode(title, StandardCharsets.UTF_8),
          URLDecoder.decode(content, StandardCharsets.UTF_8));

      return HttpResponseFactory.createRedirectResponse("/");
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}