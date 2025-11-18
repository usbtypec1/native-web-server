import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class HtmlRenderer {

  public static String readTemplateFromFile(String templateName) throws IOException {
    return Files.readString(Resources.TEMPLATES_DIR.resolve(templateName));
  }

  public static String renderWithVariables(String templateName, Map<String, String> variables) throws IOException {
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
