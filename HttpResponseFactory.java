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

  public static HttpResponse createInternalServerErrorResponse() {
    HttpHeaders headers = new HttpHeaders();
    headers.setHeader("Content-Type", "text/html; charset=UTF-8");
    return new HttpResponse(HttpResponseStatus.InternalServerError, headers, "Internal server error");
  }

  public static HttpResponse createRedirectToLoginResponse() {
    return createRedirectResponse("/login");
  }

  public static HttpResponse createRedirectTo404Response() {
    return createRedirectResponse("/404");
  }

  public static HttpResponse createRedirectTo500Response() {
    return createRedirectResponse("/500");
  }

  public static HttpResponse createBadRequestResponse(String body) {
    return new HttpResponse(HttpResponseStatus.BadRequest, null, body);
  }
}
