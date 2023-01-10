package pl.sterniczuk.passwordWallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Data
@Getter
@Setter
@Entity
public class Passwords {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String password;
    private String webAddress;
    private String description;
    private String login;
    private Boolean noEdit;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", nullable = false)
    private User user;

    public Passwords(String password, String webAddress, String description, String login, User user) {
        this.password = password;
        this.webAddress = webAddress;
        this.description = description;
        this.login = login;
        this.user = user;
    }

    public Passwords() {
    }
}
