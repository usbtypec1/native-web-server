import java.util.Map;

public abstract class HttpRequestHandler {

  protected abstract HttpResponseStatus getResponseStatus(HttpRequest request);

  protected abstract String getResponseBody(HttpRequest request);

  protected abstract Map<String, String> getResponseHeaders(HttpRequest request);

  public HttpResponse getResponse(HttpRequest request) {
    return new HttpResponse(getResponseStatus(request), getResponseHeaders(request), getResponseBody(request));
  }
}
