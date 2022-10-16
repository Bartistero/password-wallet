package pl.sterniczuk.passwordWallet.web;

import lombok.AllArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sterniczuk.passwordWallet.model.Password;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;
import pl.sterniczuk.passwordWallet.security.CustomPasswordEncoder;

@Controller
@RequestMapping("/data")
public class dataController {
    private CustomPasswordEncoder passwordEncoder;
    private UserRepository userRepository;

    @GetMapping
    public String dataForm(Model model) {
        model.addAttribute("user", new User());
        return "data";
    }

    @PostMapping("/changePassword")
    public String changePassword(User users, @AuthenticationPrincipal User user) {
        String password = users.getPassword() + user.getSalt();
        System.out.println(password);
        if (user.getType().equals("SHA512")) {
            password = passwordEncoder.encode(password, true);
        } else {
            password = passwordEncoder.encode(password, false);
        }
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/data";
    }

    public dataController(CustomPasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }
}
