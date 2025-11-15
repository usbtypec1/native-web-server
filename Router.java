import java.util.HashMap;
import java.util.Map;

public class Router {
  private final Map<String, HttpRequestHandler> getRoutes;
  private final Map<String, HttpRequestHandler> postRoutes;

  public Router() {
    getRoutes = new HashMap<>();
    postRoutes = new HashMap<>();

    getRoutes.put("/404", new NotFoundHandler());
  }

  public HttpRequestHandler match(HttpRequestMethod method, String route) {
    HttpRequestHandler handler = null;
    if (method == HttpRequestMethod.Get) {
      handler = getRoutes.get(route);
    } else if (method == HttpRequestMethod.Post) {
      handler = postRoutes.get(route);
    }

    if (handler == null) {
      handler = new RedirectHandler();
    }
    System.out.println(handler);

    return handler;
  }
}
