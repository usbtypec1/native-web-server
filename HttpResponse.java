import java.util.Map;

public class HttpResponse {
  private HttpResponseStatus status;
  private Map<String, String> headers;
  private byte[] body;

  public HttpResponse(HttpResponseStatus status, Map<String, String> headers, String body) {
    this.status = status;
    this.headers = headers;
    this.body = body.getBytes();
  }

  public HttpResponseStatus getStatus() {
    return this.status;
  }

  public Map<String, String> getHeaders() {
    return this.headers;
  }

  public byte[] getBody() {
    return this.body;
  }
}
