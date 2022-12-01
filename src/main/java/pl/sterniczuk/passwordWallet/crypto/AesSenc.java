package pl.sterniczuk.passwordWallet.crypto;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class AesSenc {
    private static final String ALGO = "AES";

    public static String encrypt(String data, String pass) throws Exception {
        byte[] keyToEncrypt = calculateMD5(pass);
        Key key = new SecretKeySpec(keyToEncrypt, ALGO);
        return encrypt(data, key);
    }

    public static String decrypt(String data, String pass) throws Exception {
        byte[] keyToEncrypt = calculateMD5(pass);
        Key key = new SecretKeySpec(keyToEncrypt, ALGO);
        return decrypt(data, key);
    }

    //encrypts string and returns encrypted string
    private static String encrypt(String data, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] encVal = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(encVal);
    }

    //decrypts string and returns plain text
    private static String decrypt(String encryptedData, Key key) throws Exception {
        Cipher c = Cipher.getInstance(ALGO);
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] decodedValue = Base64.getDecoder().decode(encryptedData);
        byte[] decValue = c.doFinal(decodedValue);
        return new String(decValue);
    }

    private static byte[] calculateMD5(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(text.getBytes());
            return messageDigest;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
