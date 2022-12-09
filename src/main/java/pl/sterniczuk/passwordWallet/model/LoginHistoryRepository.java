package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface LoginHistoryRepository extends CrudRepository<LoginHistory, Long> {
    List<LoginHistory> getAllByUserId(Long id);
}
