import java.io.IOException;

public class CreatePostGetRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    if (!request.getHeaders().hasSession()) {
      return HttpResponseFactory.createRedirectToLoginResponse();
    }
    try {
      SessionManager sessionManager = new SessionManager();
      if (!sessionManager.isAuthenticated(request.getHeaders().getSession())) {
        return HttpResponseFactory.createRedirectToLoginResponse();
      }
      return HttpResponseFactory.createOkResponse(HtmlRenderer.readTemplateFromFile("create-post.html"));
    } catch (IOException ioe) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
