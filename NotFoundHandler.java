import java.util.HashMap;
import java.util.Map;

public class NotFoundHandler extends HttpRequestHandler {

  protected HttpResponseStatus getResponseStatus() {
    return HttpResponseStatus.NotFound;
  }
  
  protected String getResponseBody() {
    return "Not found";
  }

  protected Map<String, String> getResponseHeaders() {
    Map<String, String> headers = new HashMap<>();
    return headers;
  }
}
