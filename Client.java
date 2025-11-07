import java.net.Socket;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedOutputStream;

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
        String[] lines = inputData.split("\\R");
        String[] methodAndRouteAndHttpVersion = lines[0].split("\\R");
        System.out.println(methodAndRouteAndHttpVersion[0]);
        // String method = methodAndRouteAndHttpVersion[0];
        // String route = methodAndRouteAndHttpVersion[1];
        // String httpVersion = methodAndRouteAndHttpVersion[2];

        // System.out.println(method);
        // System.out.println(route);

        String message = """
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
