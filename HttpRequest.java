import java.util.Map;

public class HttpRequest {
  private final HttpRequestMethod method;
  private final String route;
  private final HttpHeaders headers;
  private final String body;
  private final Map<String, String> form;

  public HttpRequest(HttpRequestMethod method, String route, HttpHeaders headers, String body, Map<String, String> form) {
    this.method = method;
    this.route = route;
    this.headers = headers;
    this.body = body;
    this.form = form;
  }

  public HttpRequestMethod getMethod() {
    return method;
  }

  public String getRoute() {
    return route;
  }

  public String getBody() {
    return body;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public Map<String, String> getForm() {
    return form;
  }
}
