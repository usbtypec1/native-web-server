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
    // getRoutes.put("/login", new HtmlTemplateHandler("login.html"));
    // getRoutes.put("/register", new HtmlTemplateHandler("register.html"));

    // postRoutes.put("/register", new RegisterPostRequestHandler());
    // postRoutes.put("/login", new LoginPostRequestHandler());
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
