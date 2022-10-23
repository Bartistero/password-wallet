package pl.sterniczuk.passwordWallet.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.sterniczuk.passwordWallet.model.Passwords;
import pl.sterniczuk.passwordWallet.model.PasswordRepository;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;
import pl.sterniczuk.passwordWallet.security.CustomPasswordEncoder;

@Controller
@SessionAttributes("password")
@RequestMapping("/data")
public class dataController {
    private CustomPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private PasswordRepository passwordRepository;

    @GetMapping
    public String dataForm(Model model, @AuthenticationPrincipal User user) {
        model.addAttribute("user", new User());
        model.addAttribute("password", passwordRepository.getPasswordByUser(user));
        model.addAttribute("newPass", new Passwords());
        return "data";
    }

    @PostMapping("/newPassword")
    public String addNewPassword(Passwords passwords, @AuthenticationPrincipal User user) {
        User users = userRepository.findByUsername(user.getUsername());
        passwords.setUser(users);
        passwordRepository.save(passwords);
        return "redirect:/data";
    }

    @PostMapping("/changePassword")
    public String changePassword(User newUsers, @AuthenticationPrincipal User userAuth) {
        User user = userRepository.findByUsername(userAuth.getUsername());
        String password = newUsers.getPassword() + user.getSalt();
        if (userAuth.getType().equals("SHA512")) {
            password = passwordEncoder.encode(password, true);
        } else {
            password = passwordEncoder.encode(password, false);
        }
        user.setPassword(password);
        userRepository.save(user);
        return "redirect:/data";
    }

    public dataController(CustomPasswordEncoder passwordEncoder, UserRepository userRepository, PasswordRepository passwordRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
    }
}
