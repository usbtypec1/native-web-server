import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class IndexGetRouteHttpRequestHandler extends HttpRequestHandler {
  public String getResponseBody() {
    String filePath = "templates/index.html";
    try {
      return Files.readString(Paths.get(filePath));
    } catch (IOException e) {
      e.printStackTrace();
      return "<h1>Error loading template</h1>";
    }
  }
}
