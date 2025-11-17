public class RedirectResponse extends HttpResponse {
  private String route;

  public RedirectResponse(String route) {
    super(HttpResponseStatus.SeeOther, null, null);

    if (route == null) {
      throw new IllegalArgumentException("Redirect route must not be null.");
    }
    this.route = route;
  }

  public HttpHeaders getHeaders() {
    HttpHeaders headers = super.getHeaders();
    headers.setHeader("Location", route);
    return headers;
  }
}
