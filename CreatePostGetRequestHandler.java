import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CreatePostGetRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    if (!request.getHeaders().hasSession()) {
      return HttpResponseFactory.createRedirectToLoginResponse();
    }
    try {
      SessionManager sessionManager = new SessionManager();
      String username = sessionManager.getUsername(request.getHeaders().getSession());
      if (username == null) {
        return HttpResponseFactory.createRedirectToLoginResponse();
      }
      Map<String, String> variables = new HashMap<>();
      variables.put("currentUsername", username);
      String html = HtmlRenderer.renderWithVariables("create-post.html", variables);
      return HttpResponseFactory.createOkResponse(html);
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
