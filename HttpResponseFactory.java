public class HttpResponseFactory {
  public static HttpResponse createRedirectResponse(String route) {
    if (route == null || route.isEmpty()) {
      throw new IllegalArgumentException("Route cannot be null or empty");
    }
    HttpHeaders headers = new HttpHeaders();
    headers.setHeader("Location", route);
    return new HttpResponse(HttpResponseStatus.SeeOther, headers, null);
  }

  public static HttpResponse createOkResponse(String content) {
    HttpHeaders headers = new HttpHeaders();
    headers.setHeader("Content-Type", "text/html; charset=UTF-8");
    return new HttpResponse(HttpResponseStatus.Ok, headers, content);
  }
}
