package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PasswordRepository extends CrudRepository<Passwords, Long> {
    List<Passwords> getPasswordByUser(User user);

    Passwords findPasswordsById(Long id);
}
