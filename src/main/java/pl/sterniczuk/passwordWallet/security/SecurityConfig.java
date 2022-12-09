package pl.sterniczuk.passwordWallet.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import pl.sterniczuk.passwordWallet.model.LoginHistoryRepository;
import pl.sterniczuk.passwordWallet.model.UserRepository;

import javax.servlet.http.HttpServletRequest;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private UserDetailsService userDetailsService;
    private UserRepository userRepository;
    private LoginHistoryRepository loginRepository;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/data")
                .access("hasRole('ROLE_USER')")
                .antMatchers("/", "/**").access("permitAll")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(successHandler())
                .failureHandler(failedHandler())
                .and()
                .logout()
                .logoutSuccessUrl("/")
                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")
                .and()
                .headers()
                .frameOptions()
                .sameOrigin();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new CustomPasswordEncoder(userRepository);
    }

    @Bean
    public LoginSuccessHandler successHandler() {
        return new LoginSuccessHandler(loginRepository, userRepository);
    }

    @Bean
    public LoginFailedHandler failedHandler() {
        return new LoginFailedHandler(loginRepository, userRepository);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(userDetailsService)
                .passwordEncoder(encoder());
        auth.eraseCredentials(false);
    }

    private String getClientIP(HttpServletRequest request) {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}
