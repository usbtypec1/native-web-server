import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;
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

  private String getInputData() {
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
          char symbol = (char)unicode;
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

  public void run() {
    String inputData = getInputData();
    if (inputData.isEmpty()) return;

    HttpRequest request = new HttpRequest(inputData);
    String route = request.getRoute();
    String method = request.getMethod();
    String body = request.getBody();

    if (method.equalsIgnoreCase("POST")) {
      if (route.equals("/login")) {
        Map<String, String> parsedBody = request.getParsedBody();
        UserRepository userRepository = new UserRepository("users");

        String login = parsedBody.get("login");
        String password = parsedBody.get("password");

        User user = userRepository.readUserByLogin(login);
        try {
          boolean isVerified = PasswordHasher.verify(password, user.getPasswordHash());
          if (isVerified) {
            user.setSessionId(SessionIdGenerator.generate(64));
            userRepository.updateUser(user);
            System.out.println("User session id updated.");
          } else {
            System.out.println("User is not verified.");
          }
        } catch (Exception e) {
          System.out.println("Error while verifying password");
        }
      } else if (route.equals("/register")) {
        Map<String, String> parsedBody = request.getParsedBody();
        UserRepository userRepository = new UserRepository("users");

        String login = parsedBody.get("login");
        String password = parsedBody.get("password");
        try {
          String passwordHash = PasswordHasher.hash(password);
          User user = new User(login, passwordHash, SessionIdGenerator.generate(64));
          userRepository.saveNewUser(user);
        } catch (Exception e) {
          System.out.println("Error while hashing password");
        }

      }
    }

    String message = router.readTemplate(route);

    byte[] data = message.getBytes();
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

  public void go() {
    thread.start();
  }
}
