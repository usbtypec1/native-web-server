import java.io.IOException;

public class CreatePostGetRequestHandler extends SimpleHtmlPageHandler {
  public CreatePostGetRequestHandler() {
    super("create-post.html");
  }

  public HttpResponse getResponse(HttpRequest request) {
    String session = request.getHeaders().getSession();
    if (session == null) {
      return new RedirectResponse("/login");
    }
    try {
      SessionManager sessionManager = new SessionManager(3600 * 1000);
      String username = sessionManager.getUsername(session);
      if (username == null) {
        return new RedirectResponse("/login");
      }
      return new HtmlTemplateResponse(HtmlRenderer.readTemplateFromFile(getTemplateName()));
    } catch (IOException ioe) {
      return new HttpResponse(HttpResponseStatus.InternalServerError, null, "Server error");
    }
  }
}