package pl.sterniczuk.passwordWallet.security;

import lombok.Data;
import org.springframework.security.crypto.keygen.KeyGenerators;
import pl.sterniczuk.passwordWallet.model.User;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Data
public class RegistrationForm {
    private String username;
    private String password;
    private String type;
    private boolean block;

    public User toUser(CustomPasswordEncoder passwordEncoder) {
        String salt = KeyGenerators.string().generateKey();
        password = password + salt;
        System.out.println(type);
        User user;
        if (type.equals("SHA512")) {
            user = new User(username, passwordEncoder.encode(password, TRUE), salt, type);
        } else {
            user = new User(username, passwordEncoder.encode(password, FALSE), salt, type);
        }
        user.setBlock(false);
        return user;
    }
}
