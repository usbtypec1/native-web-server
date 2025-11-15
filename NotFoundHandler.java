import java.util.HashMap;
import java.util.Map;

public class NotFoundHandler extends HttpRequestHandler {

  protected HttpResponseStatus getStatus() {
    return HttpResponseStatus.NotFound;
  }
  
  protected String getResponseBody() {
    return "Not found";
  }

  protected Map<String, String> getResponseHeaders() {
    System.out.println("Not found");
    Map<String, String> headers = new HashMap<>();

    return headers;
  }
}
