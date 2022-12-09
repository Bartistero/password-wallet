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

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDate;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private UserRepository userRepo;
    private LoginHistoryRepository loginRepository;
    private HttpServletRequest request;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo, LoginHistoryRepository loginRepository, HttpServletRequest request) {
        this.userRepo = userRepo;
        this.loginRepository = loginRepository;
        this.request = request;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
            LoginHistory loginHistory = new LoginHistory(LocalDate.now(), "True", getClientIP(), user);
            loginHistory.setUser(user);
            loginRepository.save(loginHistory);
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}