public enum HttpRequestMethod {
  Get("GET"),
  Post("POST");

  private final String value;

  HttpRequestMethod(String value) {
    this.value = value;
  }

  public boolean equals(String value) {
    return this.value.equalsIgnoreCase(value);
  }

  public static HttpRequestMethod parse(String raw) {
    if (raw == null) {
      return null;
    }

    for (HttpRequestMethod method : values()) {
      if (method.value.equalsIgnoreCase(raw)) {
        return method;
      }
    }
    return null;
  }
}
