import java.io.IOException;

public class LogoutGetRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();

    try {
      SessionManager sessionManager = new SessionManager();

      String username = sessionManager.getUsername(session);
      if (username == null) {
        return HttpResponseFactory.createRedirectResponse("/");
      }

      String html = HtmlRenderer.readTemplateFromFile("logout.html");
      return HttpResponseFactory.createOkResponse(html);
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectResponse("/500");
    }
  }
}
