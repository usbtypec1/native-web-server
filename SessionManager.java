import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SessionManager {
  private final File file;
  private final long sessionTimeoutMillis;

  public SessionManager(long sessionTimeoutMillis) throws IOException {
    this.file = new File("sessions.txt");
    this.sessionTimeoutMillis = sessionTimeoutMillis;

    if (!file.exists()) {
      file.createNewFile();
    }
  }

  public String createSession(String username) throws IOException {
    String sessionId = UUID.randomUUID().toString();
    long now = System.currentTimeMillis();

    try (BufferedWriter writer = new BufferedWriter(new FileWriter(file, true))) {
      writer.write(sessionId + ";" + username + ";" + now);
      writer.newLine();
    }

    return sessionId;
  }

  public String getUsername(String session) throws IOException {
    List<String> lines = Files.readAllLines(file.toPath());
    long now = System.currentTimeMillis();

    List<String> newContent = new ArrayList<>();
    String result = null;

    for (String line : lines) {
      String[] parts = line.split(";");
      if (parts.length != 3)
        continue;

      String id = parts[0];
      String username = parts[1];
      long issue = Long.parseLong(parts[2]);

      boolean expired = (now - issue) > sessionTimeoutMillis;

      if (!expired) {
        newContent.add(line);
      }

      if (id.equals(session) && !expired) {
        result = username;
      }
    }

    Files.write(file.toPath(), newContent);

    return result;
  }

  public void removeSession(String sessionId) throws IOException {
    List<String> lines = Files.readAllLines(file.toPath());
    List<String> activeSessions = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(";");
      if (parts.length != 3) {
        continue;
      }
      if (sessionId.equals(parts[0])) {
        continue;
      }
      activeSessions.add(line);
    }
    Files.write(file.toPath(), activeSessions);
  }

  public void cleanupExpiredSessions() throws IOException {
    long now = System.currentTimeMillis();
    List<String> lines = Files.readAllLines(file.toPath());
    List<String> activeSessions = new ArrayList<>();

    for (String line : lines) {
      String[] parts = line.split(";");
      if (parts.length != 3) {
        continue;
      }
      long issuedAt = Long.parseLong(parts[2]);
      if (now - issuedAt <= sessionTimeoutMillis) {
        activeSessions.add(line);
      }
    }
    Files.write(file.toPath(), activeSessions);
  }
}
