package pl.sterniczuk.passwordWallet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sterniczuk.passwordWallet.model.LoginHistoryRepository;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;

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
        Boolean block = user.getBlock();
        if (user != null) {
            if (block) {
                if (LocalTime.now().isAfter(user.getBlockTime().plusSeconds(30))) {
                    user.setBlock(false);
                    user.setAttempt(0);
                    user.setBlockTime(null);
                    userRepo.save(user);
                    return user;
                } else {
                    throw new UsernameNotFoundException(
                            "User '" + username + "' not found");
                }
            }
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }
}