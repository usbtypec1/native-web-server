import java.util.HashMap;
import java.util.Map;

public class RedirectHandler extends HttpRequestHandler {
  private String route;

  public RedirectHandler(String route) {
    this.route = route;
  }

  protected void setRoute(String route) {
    this.route = route;
  }

  protected HttpResponseStatus getResponseStatus(HttpRequest request) {
    return HttpResponseStatus.SeeOther;
  }

  protected String getResponseBody(HttpRequest request) {
    return "Redirecting...";
  }

  protected Map<String, String> getResponseHeaders(HttpRequest request) {
    Map<String, String> headers = new HashMap<>();
    headers.put("Location", route);
    return headers;
  }
}
