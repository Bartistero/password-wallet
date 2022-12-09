package pl.sterniczuk.passwordWallet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sterniczuk.passwordWallet.model.LoginHistory;
import pl.sterniczuk.passwordWallet.model.LoginHistoryRepository;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import java.time.LocalDate;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private UserRepository userRepo;
    private LoginHistoryRepository loginRepository;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo, LoginHistoryRepository loginRepository) {
        this.userRepo = userRepo;
        this.loginRepository = loginRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            LoginHistory loginHistory = new LoginHistory(LocalDate.now(), "True", "ip", user);
            loginHistory.setUser(user);
            loginRepository.save(loginHistory);
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }
}