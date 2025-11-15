import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

public class Client implements Runnable {
  private Socket clientSocket;
  private Thread thread;
  private Router router;

  public Client(Socket clientSocket) {
    this.clientSocket = clientSocket;
    thread = new Thread(this);
    router = new Router();
  }

  private String readDataFromSocket() {
    InputStream inputStream = null;
    try {
      inputStream = clientSocket.getInputStream();
    } catch (IOException ioe) {
      System.out.println("Error in InputStream: " + ioe);
    }

    StringBuilder stringBuilder = new StringBuilder();

    while (true) {
      try {
        if (inputStream != null) {
          int unicode = inputStream.read();
          char symbol = (char) unicode;
          stringBuilder.append(symbol);
          if (inputStream.available() == 0) {
            break;
          }
        }
      } catch (IOException ioe) {
        System.out.println("Error: " + ioe);
      }
    }

    return stringBuilder.toString();
  }

  private void writeDataToSocket(HttpResponse response) {
    byte[] data = response.getBody();
    int fileLength = data.length;
    HttpResponseStatus status = response.getStatus();

    try {
      OutputStream outputStream = clientSocket.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream, true);

      printWriter.println("HTTP/1.1 " + status.getCode() + " " + status.getReason());
      printWriter.println("Server: Java HTTP Server from Intern Labs 7.0 - Java Backend Developer");

      for (Map.Entry<String, String> header : response.getHeaders().entrySet()) {
        printWriter.println(header.getKey() + ": " + header.getValue());
      }

      printWriter.println("Content-type: text/html; charset=UTF-8");
      printWriter.println("Content-length: " + fileLength);
      printWriter.println();
      printWriter.flush();

      BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
      bufferedOutputStream.write(data, 0, fileLength);
      bufferedOutputStream.flush();
    } catch (IOException ioe) {
      System.out.println("Error: " + ioe);
    }

  }

  public void run() {
    String rawData = readDataFromSocket();
    HttpRequestParser parser = new HttpRequestParser();
    HttpRequest request = parser.parse(rawData);
    HttpRequestHandler handler = router.match(request.getMethod(), request.getRoute());
    HttpResponse response = handler.getResponse();
    writeDataToSocket(response);
  }

  public void go() {
    thread.start();
  }
}
