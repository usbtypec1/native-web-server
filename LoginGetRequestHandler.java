import java.util.Map;
import java.util.HashMap;

public class LoginGetRequestHandler extends HtmlTemplateHandler {
  protected HttpResponseStatus getResponseStatus() {
    return HttpResponseStatus.Ok;
  }

  protected Map<String, String> getResponseHeaders() {
    return new HashMap<String, String>();
  }

  protected String getTemplateName() {
    return "login.html";
  }
}
