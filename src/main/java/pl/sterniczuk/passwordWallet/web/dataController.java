package pl.sterniczuk.passwordWallet.web;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.sterniczuk.passwordWallet.crypto.AesSenc;
import pl.sterniczuk.passwordWallet.model.*;
import pl.sterniczuk.passwordWallet.security.CustomPasswordEncoder;

import java.util.List;

@Controller
@SessionAttributes("password")
@RequestMapping("/data")
public class dataController {
    private CustomPasswordEncoder passwordEncoder;
    private UserRepository userRepository;
    private PasswordRepository passwordRepository;

    private LoginHistoryRepository loginHistoryRepository;

    public dataController(CustomPasswordEncoder passwordEncoder, UserRepository userRepository, PasswordRepository passwordRepository, LoginHistoryRepository loginHistoryRepository) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.passwordRepository = passwordRepository;
        this.loginHistoryRepository = loginHistoryRepository;
    }

    @PostMapping("/newPassword")
    public String addNewPassword(Passwords passwords, @AuthenticationPrincipal User user) throws Exception {
        User users = userRepository.findByUsername(user.getUsername());
        passwords.setUser(users);

        String rawPassword = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        String pass = AesSenc.encrypt(passwords.getPassword(), rawPassword);
        passwords.setPassword(pass);

        passwordRepository.save(passwords);
        return "redirect:/data";
    }

    @PostMapping("/changePassword")
    public String changePassword(User newUsers, @AuthenticationPrincipal User userAuth) throws Exception {
        User user = userRepository.findByUsername(userAuth.getUsername());
        String password = newUsers.getPassword() + user.getSalt();
        if (userAuth.getType().equals("SHA512")) {
            password = passwordEncoder.encode(password, true);
        } else {
            password = passwordEncoder.encode(password, false);
        }

        String rawPassword = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        List<Passwords> usersPasswordList = passwordRepository.getPasswordByUser(user);
        usersPasswordList = decryptPassword(usersPasswordList, rawPassword);
        usersPasswordList = encryptPassword(usersPasswordList, newUsers.getPassword());
        user.setPassword(password);

        userRepository.save(user);
        SecurityContextHolder.clearContext();
        return "redirect:/";
    }

    @GetMapping
    public String dataForm(Model model, @AuthenticationPrincipal User user) throws Exception {
        String rawPassword = (String) SecurityContextHolder.getContext().getAuthentication().getCredentials();
        List<Passwords> passwordsList = decryptPassword(passwordRepository.getPasswordByUser(user), rawPassword);
        List<LoginHistory> loginHistory = loginHistoryRepository.getAllByUserId(user.getId());
        model.addAttribute("user", new User());
        model.addAttribute("password", passwordsList);
        model.addAttribute("newPass", new Passwords());
        model.addAttribute("loginHistory", loginHistory);
        return "data";
    }

    private List<Passwords> decryptPassword(List<Passwords> passwordsList, String rawPassword) throws Exception {
        for (Passwords passwords : passwordsList) {
            passwords.setPassword(AesSenc.decrypt(passwords.getPassword(), rawPassword));
        }
        return passwordsList;
    }

    private List<Passwords> encryptPassword(List<Passwords> passwordsList, String rawPassword) throws Exception {
        for (Passwords passwords : passwordsList) {
            passwords.setPassword(AesSenc.encrypt(passwords.getPassword(), rawPassword));
        }
        return passwordsList;
    }
}
