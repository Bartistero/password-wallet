package pl.sterniczuk.passwordWallet.security;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import static javax.xml.crypto.dsig.SignatureMethod.*;

@Service
@ConfigurationProperties(prefix = "password.wallet")
public class CustomPasswordEncoder implements PasswordEncoder {

    String pepper;
    UserRepository userRepository;

    public void setPepper(String pepper) {
        this.pepper = pepper;
    }

    @Override
    public String encode(CharSequence rawPassword) {
        return null;
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        User user = userRepository.findUserByPassword(encodedPassword);
        if (user == null) {
            return false;
        }
        String salt = user.getSalt();
        String password = rawPassword + salt + pepper;
        System.out.println(password);
        String sha512 = calculateSHA512(password);
        if (sha512.equals(encodedPassword)) {
            return true;
        } else {
            return false;
        }
    }

    @Qualifier
    public String encode(String password, Boolean isSha) {
        if (isSha) {
            return calculateSHA512(password + pepper);
        } else {
            return calculateHMAC(password + pepper, "gdfgfd");
        }
    }

    private static String calculateSHA512(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] messageDigest = md.digest(text.getBytes());
            BigInteger no = new BigInteger(1, messageDigest);
            String hashtext = no.toString(16);
            while (hashtext.length() < 32) {
                hashtext = "0" + hashtext;
            }
            return hashtext;
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static String calculateHMAC(String text, String key){
        Mac sha512Hmac;
        String result="";
        try {
            final byte[] byteKey = key.getBytes(StandardCharsets.UTF_8);
            sha512Hmac = Mac.getInstance(DSA_SHA256);
            SecretKeySpec keySpec = new SecretKeySpec(byteKey, HMAC_SHA512);
            sha512Hmac.init(keySpec);
            byte[] macData = sha512Hmac.doFinal(text.getBytes(StandardCharsets.UTF_8));
            result = Base64.getEncoder().encodeToString(macData);
        } catch (InvalidKeyException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        } finally {
        }
        return result;
    }

    public CustomPasswordEncoder(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
}
