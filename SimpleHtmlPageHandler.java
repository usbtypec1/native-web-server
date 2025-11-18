import java.io.IOException;

/**
 * A simple HTTP request handler that serves a static HTML page.
 * <p>
 * This class reads an HTML template from the file system and returns it
 * as the body of an HTTP response. If the template cannot be read, it
 * returns a 500 Internal Server Error redirect response.
 * </p>
 * 
 * <p>
 * <strong>Usage example:</strong>
 * </p>
 * 
 * <pre>{@code
 * HttpRequestHandler handler = new SimpleHtmlPageHandler("index.html");
 * HttpResponse response = handler.getResponse(request);
 * }</pre>
 */
public class SimpleHtmlPageHandler implements HttpRequestHandler {
  private final String templateName;

  public SimpleHtmlPageHandler(String templateName) {
    this.templateName = templateName;
  }

  protected String getTemplateName() {
    return templateName;
  }

  public HttpResponse getResponse(HttpRequest request) {
    try {
      return HttpResponseFactory.createOkResponse(
          HtmlRenderer.readTemplateFromFile(templateName));
    } catch (IOException e) {
      return HttpResponseFactory.createRedirectTo500Response();
    }
  }
}
