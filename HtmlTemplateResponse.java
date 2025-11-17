public class HtmlTemplateResponse extends HttpResponse {
  public HtmlTemplateResponse(String body) {
    super(HttpResponseStatus.Ok, null, body);
  }

  public HttpHeaders getHeaders() {
    HttpHeaders headers = super.getHeaders();
    headers.setHeader("Content-type", "text/html; charset=UTF-8");

    int contentLength = getContentLength();
    if (contentLength > 0) {
      headers.setHeader("Content-length", String.valueOf(getContentLength()));
    }
    return headers;
  }
}
