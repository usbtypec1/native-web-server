import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;

public class Client implements Runnable {
  private Socket clientSocket;
  private Thread thread;

  public Client(Socket clientSocket) {
    this.clientSocket = clientSocket;
    this.thread = new Thread(this);
  }

  public void run() {
    // System.out.println("Processing client.");
    // System.out.println(clientSocket);

    InputStream inputStream = null;
    try {
      inputStream = clientSocket.getInputStream();
    } catch (IOException ioe) {
      System.out.println("Error in InputStream: " + ioe);
    }

    while (true) {
      try {
        if (inputStream != null) {
          int unicode = inputStream.read();
          char symbol = (char)unicode;
          System.out.print(symbol);
          if (inputStream.available() == 0) {
            break;
          }
        }
      } catch (IOException ioe) {
        System.out.println("Error: " + ioe);
      }
    }
    // System.out.println("All data has been read from User Agent.");

    String message = """
    <!DOCTYPE html>
    <html lang="en">
    <head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login Form</title>
    <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f3f4f6;
      display: flex;
      justify-content: center;
      align-items: center;
      height: 100vh;
    }
    .login-container {
      background: white;
      padding: 2rem;
      border-radius: 10px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
      width: 300px;
    }
    h2 {
      text-align: center;
      margin-bottom: 1.5rem;
    }
    .form-group {
      margin-bottom: 1rem;
    }
    label {
      display: block;
      font-size: 0.9rem;
      margin-bottom: 0.5rem;
    }
    input[type="text"],
    input[type="password"] {
      width: 100%;
      padding: 0.6rem;
      border: 1px solid #ccc;
      border-radius: 6px;
      box-sizing: border-box;
    }
    button {
      width: 100%;
      padding: 0.7rem;
      background-color: #2563eb;
      color: white;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      font-size: 1rem;
    }
    button:hover {
      background-color: #1d4ed8;
    }
    </style>
    </head>
    <body>
    <div class="login-container">
    <h2>Login</h2>
    <form action="/login" method="POST">
    <div class="form-group">
    <label for="username">Username</label>
    <input id="username" type="text" name="username" required>
    </div>
    <div class="form-group">
    <label for="password">Password</label>
    <input id="password" type="password" name="password" required>
    </div>
    <button type="submit">Sign In</button>
    </form>
    </div>
    </body>
    </html>
    """;
    byte[] data = message.getBytes();
    int fileLength = message.length();

    try {
      OutputStream outputStream = clientSocket.getOutputStream();
      PrintWriter printWriter = new PrintWriter(outputStream, true);
      printWriter.println("HTTP/1.1 200 OK");
      printWriter.println("Server: Java HTTP Server from Intern Labs 7.0 - Java Backend Developer");
      printWriter.println("Content-type: text/html");
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
