import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public abstract class HttpRequestHandler {

  public abstract String getResponseBody(HttpRequest request);

  public void sendResponse(Socket clientSocket, HttpRequest request) {
    String responseBody = getResponseBody(request);
    byte[] data = responseBody.getBytes();
    int fileLength = data.length;

    try {
      OutputStream outputStream = clientSocket.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream, true);
      printWriter.println("HTTP/1.1 200 OK");
      printWriter.println("Server: Java HTTP Server from Intern Labs 7.0 - Java Backend Developer");
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
}
