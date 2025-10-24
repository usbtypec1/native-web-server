import java.net.Socket;
import java.io.InputStream;
import java.io.IOException;

public class Client implements Runnable {
  private Socket clientSocket;
  private Thread thread;

  public Client(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.thread = new Thread(this);
  }

  public void run() {
    System.out.println("Processing client.");
    System.out.println(clientSocket);

    InputStream inputStream = null;
    try {
      inputStream = clientSocket.getInputStream();
    } catch (IOException ioe) {
      System.out.println("Error in InputStream: " + ioe);
    }

    while (true) {
      try {
        int unicode = inputStream.read();
        char symbol = (char)unicode;
        System.out.print(symbol);
      } catch (IOException ioe) {
        System.out.println("Error: " + ioe);
      }
    }
  }

  public void go() {
    thread.start();
  }
}
