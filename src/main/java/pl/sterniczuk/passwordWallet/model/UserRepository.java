package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);

    User findUserByPassword(String password);
}
