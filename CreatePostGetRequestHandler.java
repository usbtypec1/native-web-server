import java.io.IOException;

public class CreatePostGetRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    if (session == null) {
      return HttpResponseFactory.createRedirectToLoginResponse();
    }
    try {
      SessionManager sessionManager = new SessionManager();
      String username = sessionManager.getUsername(session);
      if (username == null) {
        return HttpResponseFactory.createRedirectToLoginResponse();
      }
      return HttpResponseFactory.createOkResponse("create-post.html");
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}