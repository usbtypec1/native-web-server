public class Main {
  public static void main(String[] args) {
    int port = 8080;
    System.out.println("The server is running on http://localhost:" + port);
    WebServer webServer = new WebServer(port);
    webServer.startServer();
  }
}
