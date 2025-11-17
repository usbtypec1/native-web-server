public class HttpResponse {
  private HttpResponseStatus status;
  private HttpHeaders headers;
  private byte[] body;

  public HttpResponse(HttpResponseStatus status, HttpHeaders headers, String body) {
    this.status = status;
    this.headers = headers;
    if (body != null) {
      this.body = body.getBytes();
    }

    if (status == null) {
      this.status = HttpResponseStatus.Ok;
    }
    if (headers == null) {
      this.headers = new HttpHeaders();
    }
  }

  public HttpResponseStatus getStatus() {
    return this.status;
  }

  public HttpHeaders getHeaders() {
    return headers;
  }

  public byte[] getBody() {
    return this.body;
  }

  public int getContentLength() {
    if (body == null) {
      return 0;
    }
    return body.length;
  }
}
