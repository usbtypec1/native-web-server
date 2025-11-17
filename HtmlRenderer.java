import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class HtmlRenderer {
  private final Path templatesDir;

  public HtmlRenderer() {
    templatesDir = Paths.get("templates");
  }

  private String readTemplateFromFile(String templateName) {
    try {
      return Files.readString(templatesDir.resolve(templateName));
    } catch (IOException ioe) {
      return "<h1>Error loading template</h1>";
    }
  }

  public String render(String templateName, Map<String, String> variables) {
    String result = readTemplateFromFile(templateName);
    if (variables == null) {
      return result;
    }
    for (Map.Entry<String, String> e : variables.entrySet()) {
      String placeholder = "{{" + e.getKey() + "}}";
      result = result.replace(placeholder, e.getValue());
    }
    return result;
  }
}
