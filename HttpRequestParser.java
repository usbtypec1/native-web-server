import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpRequestParser {
  private HttpRequestMethod method;
  private String route;
  private HttpHeaders headers;
  private String body;
  private Map<String, String> form;

  public HttpRequest parse(String raw) {
    headers = new HttpHeaders();
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

    form = result;
  }

  private void parseRequestData(String raw) {
    String[] headAndBody = raw.split("\r\n\r\n", 2);
    String rawHeaders = headAndBody[0];

    if (headAndBody.length > 1) {
      body = headAndBody[1];
    }

    String[] lines = rawHeaders.split("\\R");

    // Parse request line
    if (lines.length > 0) {
        String[] reqLine = lines[0].split(" ");
        if (reqLine.length >= 3) {
            method = HttpRequestMethod.parse(reqLine[0]);
            route = reqLine[1].split("\\?")[0];
        }
    }

    // Temporary map to collect multi-value headers
    Map<String, List<String>> multi = new HashMap<>();

    // Parse headers
    for (int i = 1; i < lines.length; i++) {
        String line = lines[i];
        int idx = line.indexOf(":");
        if (idx > 0) {
            String key = line.substring(0, idx).trim();
            String value = line.substring(idx + 1).trim();

            multi.computeIfAbsent(key, k -> new ArrayList<>()).add(value);
        }
    }

    // Convert collected headers into HttpHeaders
    for (Map.Entry<String, List<String>> entry : multi.entrySet()) {
        String key = entry.getKey();
        List<String> values = entry.getValue();

        if (values.size() == 1) {
            headers.setHeader(key, values.get(0));
        } else {
            headers.setHeader(key, values.toArray(new String[0]));
        }
    }
  }
}
