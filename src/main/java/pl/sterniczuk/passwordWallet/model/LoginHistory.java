package pl.sterniczuk.passwordWallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Entity
public class LoginHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private LocalDate date;
    private String type;
    private String ip;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    public LoginHistory(LocalDate date, String type, String ip, User user) {
        this.date = date;
        this.type = type;
        this.ip = ip;
        this.user = user;
    }

    public LoginHistory() {
    }
}
