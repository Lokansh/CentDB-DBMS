package authentication.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HashingService {

    public static String hashText(String text) {
        String hash = null;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");

            messageDigest.update(text.getBytes());

            /* Convert the hash value into bytes */
            byte[] bytes = messageDigest.digest();

            /* The bytes array has bytes in decimal form. Converting it into hexadecimal format. */
            StringBuilder s = new StringBuilder();
            for (byte aByte : bytes) {
                s.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
            }

            /* Complete hashed password in hexadecimal format */
            hash = s.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return hash;
    }
}
