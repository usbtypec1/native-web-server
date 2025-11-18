import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;

public class CreatePostPostRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    Map<String, String> form = request.getForm();
    String title = form.get("title");
    String content = form.get("content");

    try {
      if (title == null || title.isBlank() || content == null || content.isBlank()) {
        String errorMessage = """
            <div class="w-full max-w-xl mb-4">
              <span class="block bg-red-50 border border-red-200 text-red-700 px-4 py-3 rounded-lg text-sm flex items-center gap-2">
                <i class="fa-solid fa-circle-exclamation"></i> Title and content cannot be empty.
              </span>
            </div>""";
        String body = HtmlRenderer.renderWithVariables("create-post.html", Map.of("errorMessage", errorMessage));
        return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
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
      Post post = new Post(UUID.randomUUID(), username, URLDecoder.decode(title, StandardCharsets.UTF_8),
          URLDecoder.decode(content, StandardCharsets.UTF_8), Instant.now());
      postRepository.createPost(post);

      return HttpResponseFactory.createRedirectResponse("/");
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}