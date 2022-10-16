package pl.sterniczuk.passwordWallet.security;

import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sterniczuk.passwordWallet.model.User;

@Data
public class RegistrationForm {

    private String username;
    private String password;
    private String type;

    public User toUser(PasswordEncoder passwordEncoder) {
        return new User(username, passwordEncoder.encode(password));
    }
}
