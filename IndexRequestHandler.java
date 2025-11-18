import java.io.IOException;
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

      String html = HtmlRenderer.renderWithVariables("index.html", Map.of("posts", postsTemplateBuilder.toString()));
      return HttpResponseFactory.createOkResponse(html);
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
