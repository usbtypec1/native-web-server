import java.util.HashMap;
import java.util.Map;

public class Router {
  private final Map<String, HttpRequestHandler> routes;

  public Router() {
    routes = new HashMap<>();
    registerHttpRequestHandler("/", "GET", new IndexGetRouteHttpRequestHandler());
    registerHttpRequestHandler("/not-found", "GET", new NotFoundGetRouteHttpRequestHandler());
  }

  private void registerHttpRequestHandler(String route, String method, HttpRequestHandler handler) {
    String key = buildRouteKey(route, method);
    if (routes.containsKey(key)) {
      System.err.println("Route already registered.");
    }
    routes.put(key, handler);
  }

  private String buildRouteKey(String route, String method) {
    return route + "@" + method;
  }

  private HttpRequestHandler getNotFoundHandler() {
    return routes.get(buildRouteKey("/not-found", "GET"));
  }

  public HttpRequestHandler getHandler(String route, String method) {
    String key = buildRouteKey(route, method);
    return routes.getOrDefault(key, getNotFoundHandler());
  }
}
