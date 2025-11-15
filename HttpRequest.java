import java.util.Map;

public class HttpRequest {
  private HttpRequestMethod method;
  private String route;
  private Map<String, String> headers;
  private String body;
  private Map<String, String> form;

  public HttpRequest(HttpRequestMethod method, String route, Map<String, String> headers, String body, Map<String, String> form) {
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

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Map<String, String> getForm() {
    return form;
  }
}
