import java.net.ServerSocket;
import java.io.IOException;
import java.net.Socket;

public class WebServer {
  private ServerSocket serverSocket;
  private Object lock;

  public WebServer(int port) {
    try {
      serverSocket = new ServerSocket(port);
      lock = new Object();
    } catch (IOException ioe) {
      System.out.println("Server start error: " + ioe);
    }
  }

  public void startServer() {
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        synchronized(lock) {
          Client client = new Client(socket);
          client.go();
        }
      } catch (IOException ioe) {
        System.out.println("Socket error: " + ioe);
      }
    }
  }

  public void stopServer() {
  }
}
