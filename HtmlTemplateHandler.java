import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class HtmlTemplateHandler extends HttpRequestHandler {
  private String templateName;

  public HtmlTemplateHandler(String templateName) {
    this.templateName = templateName;
  }

  protected HttpResponseStatus getResponseStatus(HttpRequest request) {
    return HttpResponseStatus.Ok;
  }

  protected String getTemplateName() {
    return templateName;
  }

  protected Map<String, String> getResponseHeaders(HttpRequest request) {
    return new HashMap<String, String>();
  }

  protected String getResponseBody(HttpRequest request) {
    try {
      return Files.readString(Path.of("templates", getTemplateName()));
    } catch (IOException ioe) {
      return "<h1>Error loading template</h1>";
    }
  }
}
