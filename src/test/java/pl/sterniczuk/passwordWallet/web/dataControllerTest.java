package pl.sterniczuk.passwordWallet.web;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.sterniczuk.passwordWallet.model.PasswordRepository;
import pl.sterniczuk.passwordWallet.model.Passwords;
import pl.sterniczuk.passwordWallet.model.User;
import pl.sterniczuk.passwordWallet.model.UserRepository;
import pl.sterniczuk.passwordWallet.security.CustomPasswordEncoder;

import java.awt.print.Paper;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class dataControllerTest {

    private CustomPasswordEncoder passwordEncoder;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordRepository passwordRepository;
    @Mock
    private Authentication auth;
    private dataController dataController;
    private Passwords passwords;

    @BeforeEach
    public void setMocks() {
        Paper paper = new Paper();
        passwords = new Passwords("ass", "sdfs", "sdfsdfs", "sdfsdf", prepareUser());
        userRepository = mock(UserRepository.class);
        passwordRepository = mock(PasswordRepository.class);
        passwordEncoder = new CustomPasswordEncoder(userRepository);
        auth = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(auth);
        dataController = new dataController(passwordEncoder, userRepository, passwordRepository);
    }

    @Test
    public void addNewPassword_positive() throws Exception {

        when(userRepository.findByUsername("imie")).thenReturn(prepareUser());
        when(passwordRepository.save(any(Passwords.class))).thenReturn(passwords);
        when(auth.getCredentials()).thenReturn("mockedPassword");
        Assertions.assertEquals(dataController.addNewPassword(passwords, prepareUser()), "redirect:/data");

    }

    @Test
    public void changePassword_positive() throws Exception {

        when(userRepository.findByUsername("imie")).thenReturn(prepareUser());
        when(passwordRepository.save(any(Passwords.class))).thenReturn(passwords);
        when(auth.getCredentials()).thenReturn("mockedPassword");
        Assertions.assertEquals(dataController.changePassword(prepareUser(), prepareUser()), "redirect:/");
    }

    private User prepareUser() {
        return new User("imie", "zaq1@WSX", "f1473fb11057a9b9", "SHA512");
    }
}
