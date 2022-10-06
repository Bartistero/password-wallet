package pl.sterniczuk.passwordWallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Password extends IdModel {

    String Password;
    Long idUser;
    String webAddress;
    String Description;
    String Login;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", nullable = false, insertable = false, updatable = false)
    private User user;
}
