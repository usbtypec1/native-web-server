import java.util.HashMap;
import java.util.Map;

public class NotFoundHandler extends HtmlTemplateHandler {

  protected HttpResponseStatus getResponseStatus() {
    return HttpResponseStatus.NotFound;
  }
  
  protected String getTemplateName() {
    return "not-found.html";
  }

  protected Map<String, String> getResponseHeaders() {
    Map<String, String> headers = new HashMap<>();
    return headers;
  }
}
