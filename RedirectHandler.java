import java.util.HashMap;
import java.util.Map;

public class RedirectHandler extends HttpRequestHandler {

  protected HttpResponseStatus getResponseStatus() {
    return HttpResponseStatus.Found;
  }
  
  protected String getResponseBody() {
    return "Redirecting...";
  }

  protected Map<String, String> getResponseHeaders() {
    Map<String, String> headers = new HashMap<>();
    headers.put("Location", "/404");

    return headers;
  }
}
