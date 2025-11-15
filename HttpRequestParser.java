import java.util.HashMap;
import java.util.Map;

public class HttpRequestParser {
  private String method;
  private String route;
  private Map<String, String> headers;
  private String body;
  private Map<String, String> form;

  public HttpRequest parse(String raw) {
    headers = new HashMap<>();
    parseRequestData(raw);
    parseForm(body);
    return new HttpRequest(method, route, headers, body, form);
  }

  private void parseForm(String body) {
    Map<String, String> result = new HashMap<>();

    if (body == null || body.isEmpty()) {
      return;
    }

    String[] parts = body.split("&");
    for (String part : parts) {
      String[] kv = part.split("=", 2);
      if (kv.length == 2) {
        result.put(kv[0], kv[1]);
      }
    }
  }

  private void parseRequestData(String raw) {
    String[] headAndBody = raw.split("\r\n\r\n", 2);
    String rawHeaders = headAndBody[0];

    if (headAndBody.length > 1) {
      body = headAndBody[1];
    }

    String[] lines = rawHeaders.split("\\R");

    if (lines.length > 0) {
      String[] reqLine = lines[0].split(" ");
      if (reqLine.length >= 3) {
        method = reqLine[0];
        route = reqLine[1].split("\\?")[0];
      }
    }

    for (int i = 1; i < lines.length; i++) {
      String line = lines[i];
      int idx = line.indexOf(":");
      if (idx > 0) {
        String key = line.substring(0, idx).trim();
        String value = line.substring(idx + 1).trim();
        headers.put(key, value);
      }
    }
  }
}
