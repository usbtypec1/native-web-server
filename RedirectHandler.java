import java.util.HashMap;
import java.util.Map;

public class RedirectHandler extends HttpRequestHandler {
  private String route;

  public RedirectHandler(String route) {
    this.route = route;
  }

  protected HttpResponseStatus getResponseStatus() {
    return HttpResponseStatus.SeeOther;
  }

  protected String getResponseBody() {
    return "Redirecting...";
  }

  protected Map<String, String> getResponseHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Location", route);
    return headers;
  }
}
