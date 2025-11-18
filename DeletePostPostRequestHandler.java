import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class DeletePostPostRequestHandler implements HttpRequestHandler {
  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    String currentUsername = null;

    try {
      SessionManager sessionManager = new SessionManager();
      currentUsername = sessionManager.getUsername(session);
      Map<String, String> form = request.getForm();
      String rawPostId = form.get("postId");

      UUID postId;
      try {
        postId = UUID.fromString(rawPostId);
      } catch (IllegalArgumentException e) {
        return HttpResponseFactory.createRedirectTo404Response();
      }

      PostRepository postRepository = new PostRepository();
      try {
        Post post = postRepository.getPostById(postId);
        if (post.getUsername().equals(currentUsername)) {
          postRepository.deletePost(postId);
        }
      } catch (PostNotFoundException e) {
        return HttpResponseFactory.createRedirectTo404Response();
      }
      return HttpResponseFactory.createRedirectResponse("/");
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
