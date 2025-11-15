public enum HttpResponseStatus {
  Ok(200, "OK"),
  Found(302, "Found"),
  SeeOther(303, "See Other"),
  Unauthorized(401, "Unauthorized"),
  Forbidden(403, "Forbidden"),
  NotFound(404, "Not Found"),
  InternalServerError(500, "Internal Server Error");

  private final int code;
  private final String reason;

  HttpResponseStatus(int code, String reason) {
    this.code = code;
    this.reason = reason;
  }

  public int getCode() {
    return code;
  }

  public String getReason() {
    return reason;
  }
}
