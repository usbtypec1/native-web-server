import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public abstract class HtmlTemplateHandler extends HttpRequestHandler {

  protected abstract String getTemplateName();

  protected String getResponseBody() {
    try {
      return Files.readString(Path.of("templates", getTemplateName()));
    } catch (IOException ioe) {
      return "<h1>Error loading template</h1>";
    }
  }
}
