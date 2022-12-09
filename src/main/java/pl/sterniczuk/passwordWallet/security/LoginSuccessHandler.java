package pl.sterniczuk.passwordWallet.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.sterniczuk.passwordWallet.model.LoginHistory;
import pl.sterniczuk.passwordWallet.model.LoginHistoryRepository;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

@Component
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private LoginHistoryRepository loginRepository;
    private UserRepository userRepository;
    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    public LoginSuccessHandler(LoginHistoryRepository loginRepository, UserRepository userRepository) {
        this.loginRepository = loginRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws ServletException, IOException {
        //super.onAuthenticationSuccess(request, response, authentication);
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        LoginHistory loginHistory = new LoginHistory(LocalDate.now(), "Poprawne", getClientIP(request), user);
        loginRepository.save(loginHistory);
        redirectStrategy.sendRedirect(request, response, "/data");
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
