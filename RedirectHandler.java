public class RedirectHandler implements HttpRequestHandler {
  private String route;

  public RedirectHandler(String route) {
    this.route = route;
  }

  protected void setRoute(String route) {
    this.route = route;
  }

  public HttpResponse getResponse(HttpRequest request) {
    return new RedirectResponse(route);
  }
}
