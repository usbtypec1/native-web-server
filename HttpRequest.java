import java.util.HashMap;
import java.util.Map;

public class HttpRequest {
  private String method;
  private String route;
  private String httpVersion;
  private String session;
  private Map<String, String> headers;
  private String body;
  private Map<String, String> parsedBody;

  public HttpRequest(String rawRequestData) {
    headers = new HashMap<>();
    parseRequestData(rawRequestData);
    parseBody();
  }

  public String getMethod() {
    return method;
  }

  public String getRoute() {
    return route;
  }

  public String getHttpVersion() {
    return httpVersion;
  }

  public String getSession() {
    return session;
  }

  public String getBody() {
    return body;
  }

  public Map<String, String> getHeaders() {
    return headers;
  }

  public Map<String, String> getParsedBody() {
    return parsedBody;
  }

  private void parseBody() {
    parsedBody = new HashMap<>();
    if (body == null || body.isEmpty()) return;
    String[] parts = body.split("&");
    for (String part : parts) {
      String[] keyAndValue = part.split("=");
      String key = keyAndValue[0];
      String value = keyAndValue[1];
      parsedBody.put(key, value);
    }
  }

  private void parseRequestData(String rawRequestData) {
    System.err.println("Request: " + rawRequestData);
    String[] headersAndBody = rawRequestData.split("\r\n\r\n", 2);
    String rawHeaders = headersAndBody[0];
    if (headersAndBody.length > 1) {
      body = headersAndBody[1];
    } else {
      body = "";
    }

    String[] headersLines = rawHeaders.split("\\R");
    if (headersLines.length > 0) {
      String[] requestLine = headersLines[0].split(" ");
      if (requestLine.length >= 3) {
        method = requestLine[0];
        route = requestLine[1];
        if (route != null) {
          route = route.split("\\?")[0];
        }
        httpVersion = requestLine[2];
      }
    }

    // Parse headers
    for (int i = 1; i < headersLines.length; i++) {
      String line = headersLines[i];
      int colonIndex = line.indexOf(":");
      if (colonIndex > 0) {
        String key = line.substring(0, colonIndex).trim();
        String value = line.substring(colonIndex + 1).trim();
        headers.put(key, value);
      }
    }

    // Extract session from cookies
    if (headers.containsKey("Cookie")) {
      String cookies = headers.get("Cookie");
      for (String cookie : cookies.split(";")) {
        String[] kv = cookie.trim().split("=");
        if (kv.length == 2 && kv[0].equals("session")) {
          session = kv[1];
          break;
        }
      }
    }
  }
}
