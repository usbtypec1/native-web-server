import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class HtmlRenderer {
  private static final Path templatesDir = Paths.get("templates");

  public static String readTemplateFromFile(String templateName) {
    try {
      return Files.readString(templatesDir.resolve(templateName));
    } catch (IOException ioe) {
      return "<h1>Error loading template</h1>";
    }
  }

  public static String renderWithVariables(String templateName, Map<String, String> variables) {
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
