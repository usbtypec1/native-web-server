import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Base64.Encoder;
import java.security.MessageDigest;

public class PasswordHasher {
    private static final int KEY_LENGTH = 256;
    private static final int ITERATIONS = 200_000;
    private static final SecureRandom RNG = new SecureRandom();

    public static String hash(String password) throws Exception {
        byte[] salt = new byte[16];
        RNG.nextBytes(salt);
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();

        Encoder encoder = Base64.getEncoder();
        String encodedSalt = encoder.encodeToString(salt);
        String encodedPasswordHash = encoder.encodeToString(hash);

        return ITERATIONS + ":" + encodedSalt + ":" + encodedPasswordHash;
    }

    public static boolean verify(String password, String stored) throws Exception {
        String[] parts = stored.split(":");
        int iterations = Integer.parseInt(parts[0]);
        byte[] salt = Base64.getDecoder().decode(parts[1]);
        byte[] hash = Base64.getDecoder().decode(parts[2]);

        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, hash.length * 8);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] testHash = skf.generateSecret(spec).getEncoded();

        return MessageDigest.isEqual(hash, testHash);
    }
}
