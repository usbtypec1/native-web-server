import java.util.HashMap;
import java.util.Map;

public class Router {
  private final Map<String, HttpRequestHandler> getRoutes;
  private final Map<String, HttpRequestHandler> postRoutes;

  public Router() {
    getRoutes = new HashMap<>();
    postRoutes = new HashMap<>();

    getRoutes.put("/", new IndexRequestHandler());
    getRoutes.put("/404", new SimpleHtmlPageHandler("not-found.html"));
    getRoutes.put("/login", new SimpleHtmlPageHandler("login.html"));
    getRoutes.put("/register", new SimpleHtmlPageHandler("register.html"));
    getRoutes.put("/posts/create", new CreatePostGetRequestHandler());

    postRoutes.put("/register", new RegisterPostRequestHandler());
    postRoutes.put("/login", new LoginPostRequestHandler());
    postRoutes.put("/posts/create", new CreatePostPostRequestHandler());
  }

  public HttpRequestHandler match(HttpRequestMethod method, String route) {
    System.out.println(method.getValue() + " " + route);

    HttpRequestHandler handler = null;
    if (method == HttpRequestMethod.Get) {
      handler = getRoutes.get(route);
    } else if (method == HttpRequestMethod.Post) {
      handler = postRoutes.get(route);
    }

    if (handler == null) {
      handler = new RedirectHandler("/404");
    }

    return handler;
  }
}
