import java.util.Map;

public abstract class HttpRequestHandler {

  protected abstract HttpResponseStatus getResponseStatus();

  protected abstract String getResponseBody();

  protected abstract Map<String, String> getResponseHeaders();

  public HttpResponse getResponse() {
    return new HttpResponse(getResponseStatus(), getResponseHeaders(), getResponseBody());
  }
}
