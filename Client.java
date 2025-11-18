import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Client implements Runnable {
  private final Socket clientSocket;
  private final Thread thread;
  private final Router router;

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
    HttpResponseStatus status = response.getStatus();

    try {
      OutputStream outputStream = clientSocket.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream, true);

      printWriter.println("HTTP/1.1 " + status.getCode() + " " + status.getReason());
      printWriter.println("Server: Java HTTP Server from Intern Labs 7.0 by Eldos Baktybek uulu - Java Backend Developer");

      for (String line : response.getHeaders().toLines()) {
        printWriter.println(line);
      }
      printWriter.println();
      printWriter.flush();

      int contentLength = response.getContentLength();
      if (contentLength > 0) {
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(outputStream);
        bufferedOutputStream.write(response.getBody(), 0, contentLength);
        bufferedOutputStream.flush();
      }
    } catch (IOException ioe) {
      System.out.println("Error: " + ioe);
    }
  }

  public void run() {
    try {
      String rawData = readDataFromSocket();
      HttpRequestParser parser = new HttpRequestParser();
      HttpRequest request = parser.parse(rawData);
      HttpRequestHandler handler = router.match(request.getMethod(), request.getRoute());
      HttpResponse response = handler.getResponse(request);
      writeDataToSocket(response);
    } finally {
      try {
        clientSocket.close();
      } catch (IOException ioe) {
        System.err.println("Could not close client socket: " + ioe);
      }
    }
  }

  public void go() {
    thread.start();
  }
}
