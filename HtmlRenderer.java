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

    String currentUsername = variables.remove("currentUsername");
    boolean isLoggedIn = currentUsername != null;
    result = result.replace("{{header}}", HeaderTemplate.getTemplate(isLoggedIn));
    
    for (Map.Entry<String, String> e : variables.entrySet()) {
      if (e.getValue() == null) {
        continue;
      }
      String placeholder = "{{" + e.getKey() + "}}";
      result = result.replace(placeholder, e.getValue());
    }
    return result;
  }
}
