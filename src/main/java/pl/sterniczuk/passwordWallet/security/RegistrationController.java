package pl.sterniczuk.passwordWallet.security;

import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import static java.lang.Boolean.TRUE;

@Controller
@AllArgsConstructor
@RequestMapping("/register")
public class RegistrationController {

    private UserRepository userRepository;
    private CustomPasswordEncoder passwordEncoder;

    @GetMapping
    public String registerForm(Model model) {
        return "registration";
    }

    @PostMapping
    public String processRegistration(RegistrationForm form) {
        userRepository.save(form.toUser(passwordEncoder));
        return "redirect:/login";
    }
}
