import java.util.Map;

public abstract class HttpRequestHandler {

  protected abstract HttpResponseStatus getStatus();

  protected abstract String getResponseBody();

  protected abstract Map<String, String> getResponseHeaders();

  public HttpResponse getResponse() {
    return new HttpResponse(getStatus(), getResponseHeaders(), getResponseBody());
  }
}
