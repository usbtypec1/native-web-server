/**
 * Represents supported HTTP request methods used by the server.
 * <p>
 * This enum provides helper methods for comparing raw method names and
 * parsing incoming HTTP request strings into a corresponding enum value.
 */
public enum HttpRequestMethod {
  Get, Post;

  /**
   * Returns the uppercase string representation of this method
   * as used in HTTP request lines.
   *
   * @return the method name in uppercase (e.g., {@code "GET"})
   */
  public String getValue() {
    return this.toString().toUpperCase();
  }

  /**
   * Compares the provided string with this method's name,
   * ignoring case differences.
   *
   * @param value the method name to compare
   * @return {@code true} if the names match ignoring case, otherwise {@code false}
   */
  public boolean equals(String value) {
    return this.toString().equalsIgnoreCase(value);
  }

  /**
   * Attempts to parse the given raw string into a {@link HttpRequestMethod}.
   * <p>
   * Matching is case-insensitive. If the string does not match any known
   * HTTP method, this method returns {@code null}.
   *
   * @param raw the raw method name extracted from an HTTP request
   * @return the matching {@link HttpRequestMethod}, or {@code null} if unrecognized
   */
  public static HttpRequestMethod parse(String raw) {
    if (raw == null) {
      return null;
    }

    for (HttpRequestMethod method : values()) {
      if (method.toString().equalsIgnoreCase(raw)) {
        return method;
      }
    }
    return null;
  }
}
