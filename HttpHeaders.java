import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * A simple container for storing and formatting HTTP headers.
 * <p>
 * This class provides methods for setting headers with either a single value
 * or multiple values, and converting all stored headers into an array of
 * HTTP-compliant header lines.
 * </p>
 *
 * <p>
 * <strong>Usage example:</strong>
 * </p>
 * 
 * <pre>{@code
 * HttpHeaders headers = new HttpHeaders();
 * headers.setHeader("Content-Type", "application/json");
 * headers.setHeader("Set-Cookie", new String[] { "a=1", "b=2" });
 * String[] lines = headers.toLines();
 * }</pre>
 */
public class HttpHeaders {

  private final Map<String, String> headers;

  /**
   * Creates an empty HTTP header container.
   */
  public HttpHeaders() {
    this.headers = new HashMap<>();
  }

  /**
   * Sets a header with a single value.
   *
   * @param key   the name of the header; ignored if {@code null}
   * @param value the header value; ignored if {@code null}
   */
  public void setHeader(String key, String value) {
    if (key == null || value == null)
      return;
    headers.put(key, value);
  }

  /**
   * Sets a header with multiple values. Values are joined using ", "
   * as required by standard HTTP multi-value header formatting.
   *
   * @param key    the header name; ignored if {@code null}
   * @param values the array of values; ignored if {@code null}
   */
  public void setHeader(String key, String[] values) {
    if (key == null || values == null)
      return;
    StringJoiner joiner = new StringJoiner(", ");
    for (String v : values) {
      if (v != null)
        joiner.add(v);
    }
    headers.put(key, joiner.toString());
  }

  /**
   * Extracts a session ID from the "Cookie" header.
   *
   * <p>
   * This method looks for a cookie named "session" and returns its value.
   * If the cookie does not exist or the Cookie header is missing, it returns
   * {@code null}.
   * </p>
   *
   * @return the session ID from cookies, or {@code null} if not present
   */
  public String getSession() {
    String cookieHeader = headers.get("Cookie");
    if (cookieHeader == null)
      return null;

    String[] pairs = cookieHeader.split(";");
    for (String pair : pairs) {
      String[] kv = pair.trim().split("=", 2);
      if (kv.length == 2) {
        String key = kv[0].trim();
        String value = kv[1].trim();

        if (key.equalsIgnoreCase("session")) {
          return value;
        }
      }
    }

    return null;
  }

  /**
   * Checks if a session cookie is present in the headers.
   * @return true if a session cookie exists, false otherwise
   */
  public boolean hasSession() {
    return getSession() != null;
  }

  /**
   * Sets a session cookie in the headers.
   *
   * <p>
   * This method creates a "Set-Cookie" header for a session cookie with the
   * specified session ID and maximum age.
   * </p>
   *
   * @param session       the session ID to set; ignored if {@code null}
   * @param maxAgeSeconds the maximum age of the cookie in seconds
   */
  public void setSession(String session, int maxAgeSeconds) {
    if (session == null)
      return;
    String cookie = String.format("session=%s; Path=/; HttpOnly; Max-Age=%s", session, maxAgeSeconds);
    setHeader("Set-Cookie", cookie);
  }

  /**
   * Converts all stored headers to an array of raw HTTP header lines.
   * <p>
   * Each element of the returned array has the format:
   * <br>
   * {@code "Header-Name: value"}
   * </p>
   *
   * @return an array of header lines ready for HTTP transmission
   */
  public String[] toLines() {
    String[] lines = new String[headers.size()];
    int i = 0;
    for (Map.Entry<String, String> entry : headers.entrySet()) {
      lines[i++] = entry.getKey() + ": " + entry.getValue();
    }
    return lines;
  }
}
