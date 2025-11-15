import java.util.HashMap;
import java.util.Map;

public class Router {
  private final Map<String, HttpRequestHandler> getRoutes;
  private final Map<String, HttpRequestHandler> postRoutes;

  public Router() {
    getRoutes = new HashMap<>();
    postRoutes = new HashMap<>();
  }

  public HttpRequestHandler match(String method, String route) {
    if (method.equalsIgnoreCase("POST")) {
      return postRoutes.get(route);
    }
    return getRoutes.get(route);
  }
}
