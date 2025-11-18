import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class DeletePostPostRequestHandler implements HttpRequestHandler {
  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    String currentUsername = null;
    try {
      SessionManager sessionManager = new SessionManager(3600 * 1000);
      currentUsername = sessionManager.getUsername(session);
    } catch (IOException e) {
      e.printStackTrace();
    }

    Map<String, String> form = request.getForm();
    String rawPostId = form.get("postId");

    UUID postId;
    try {
      postId = UUID.fromString(rawPostId);
    } catch (IllegalArgumentException e) {
      return new RedirectResponse("/404");
    }

    PostRepository postRepository = new PostRepository();
    try {
      Post post = postRepository.getPostById(postId);
      if (post.getUsername().equals(currentUsername)) {
        postRepository.deletePost(postId);
      }
    } catch (StorageException | PostNotFoundException e) {
      e.printStackTrace();
      return new RedirectResponse("/500");
    }

    return new RedirectResponse("/");
  }

}
