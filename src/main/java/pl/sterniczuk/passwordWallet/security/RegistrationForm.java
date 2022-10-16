package pl.sterniczuk.passwordWallet.security;

import lombok.Data;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.keygen.KeyGenerators;
import pl.sterniczuk.passwordWallet.model.User;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String type;

    public User toUser(CustomPasswordEncoder passwordEncoder) {
        String salt = KeyGenerators.string().generateKey();
        password = password + salt;
        System.out.println(type);
        if (type.equals("SHA512")) {
            return new User(username, passwordEncoder.encode(password, TRUE), salt, type);
        } else {
            return new User(username, passwordEncoder.encode(password, FALSE), salt, type);
        }
    }
}
