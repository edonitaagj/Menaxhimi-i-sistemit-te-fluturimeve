package services;

import org.mindrot.jbcrypt.BCrypt;

public class HashService {

    private static final int WORK_FACTOR = 12; // kostoja e kompjutimit (2^12 iterata)

    public static String generateHash(String password) {
        String salt = BCrypt.gensalt(WORK_FACTOR);
        return BCrypt.hashpw(password, salt);
        // gjithqka në një string
    }

    public static boolean validatePassword(String password, String storedHash) {
        return BCrypt.checkpw(password, storedHash);
        // BCrypt e nxjerr salt-in vet nga storedHash
    }
}