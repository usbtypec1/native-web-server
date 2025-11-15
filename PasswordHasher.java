import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordHasher {
  private static final int KEY_LENGTH = 256;
  private static final int ITERATIONS = 200_000;
  private static final SecureRandom RNG = new SecureRandom();

  public static String hash(String password) {
    byte[] salt = new byte[16];
    RNG.nextBytes(salt);
    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
    SecretKeyFactory skf = null;
    try {
      skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Invalid hashing algorithm:" + e);
      return null;
    }

    byte[] hash = null;
    try {
      hash = skf.generateSecret(spec).getEncoded();
    } catch (InvalidKeySpecException e) {
      System.err.println("Invalid key spec:" + e);
      return null;
    }
    Encoder encoder = Base64.getEncoder();
    String encodedSalt = encoder.encodeToString(salt);
    String encodedPasswordHash = encoder.encodeToString(hash);

    return ITERATIONS + ":" + encodedSalt + ":" + encodedPasswordHash;
  }

  public static boolean verify(String password, String stored) {
    String[] parts = stored.split(":");
    int iterations = Integer.parseInt(parts[0]);
    byte[] salt = Base64.getDecoder().decode(parts[1]);
    byte[] hash = Base64.getDecoder().decode(parts[2]);

    PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);

    SecretKeyFactory skf = null;
    try {
      skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
    } catch (NoSuchAlgorithmException e) {
      System.err.println("Invalid hashing algorithm:" + e);
      return false;
    }

    byte[] testHash = null;
    try {
      testHash = skf.generateSecret(spec).getEncoded();
    } catch (InvalidKeySpecException e) {
      System.err.println("Invalid key spec:" + e);
      return false;
    }

    return MessageDigest.isEqual(hash, testHash);
  }
}
