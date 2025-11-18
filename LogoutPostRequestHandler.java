import java.io.IOException;

public class LogoutPostRequestHandler implements HttpRequestHandler {

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();

    try {
      SessionManager sessionManager = new SessionManager();

      String username = sessionManager.getUsername(session);
      if (username != null) {
        sessionManager.removeSession(session);
        sessionManager.cleanupExpiredSessions();
      }
      return HttpResponseFactory.createRedirectResponse("/");
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
