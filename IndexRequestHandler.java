import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class IndexRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    try {
      SessionManager sessionManager = new SessionManager();
      String currentUsername = sessionManager.getUsername(session);

      PostRepository postRepository = new PostRepository();
      Post[] posts = postRepository.getAllPosts();

      PostsTemplateBuilder postsTemplateBuilder = new PostsTemplateBuilder();
      for (Post post : posts) {
        postsTemplateBuilder.appendPost(post, currentUsername);
      }

      Map<String, String> variables = new HashMap<>();
      variables.put("posts", postsTemplateBuilder.toString());
      variables.put("currentUsername", currentUsername);

      String html = HtmlRenderer.renderWithVariables("index.html", variables);
      return HttpResponseFactory.createOkResponse(html);
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
