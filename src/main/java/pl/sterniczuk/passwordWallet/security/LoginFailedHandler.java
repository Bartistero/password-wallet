package pl.sterniczuk.passwordWallet.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import pl.sterniczuk.passwordWallet.model.LoginHistory;
import pl.sterniczuk.passwordWallet.model.LoginHistoryRepository;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;

public class LoginFailedHandler implements AuthenticationFailureHandler {
    private LoginHistoryRepository loginRepository;
    private UserRepository userRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LoginFailedHandler(LoginHistoryRepository loginRepository, UserRepository userRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
        String username = request.getParameter("username");
        User user = userRepository.findByUsername(username);
        if (user != null) {
            LoginHistory loginHistory = new LoginHistory(LocalDate.now(), "Niepoprawne", getClientIP(request), user);
            loginRepository.save(loginHistory);
            int attempt = user.getAttempt();
            attempt++;
            user.setAttempt(attempt);
            if (attempt >= 3) {
                user.setBlock(true);
                user.setBlockTime(LocalTime.now());
            }
            userRepository.save(user);
        }
        redirectStrategy.sendRedirect(request, response, "/login");
    }
}
