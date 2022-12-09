package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

public interface LoginHistoryRepository extends CrudRepository<LoginHistory, Long> {
}
