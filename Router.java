import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class Router {
    private Map<String, String> routes;

    public Router() {
        routes = new HashMap<>();
        routes.put("/", "index");
        routes.put("/login", "login");
    }

    public String getTemplateName(String route) {
        return routes.get(route);
    }

    public String readTemplate(String route) {
        String templateName = getTemplateName(route);
        if (templateName == null) {
            return "<h1>404 Not Found</h1>";
        }

        String filePath = "templates/" + templateName + ".html";
        try {
            return Files.readString(Paths.get(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            return "<h1>Error loading template</h1>";
        }
    }
}
