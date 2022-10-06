package pl.sterniczuk.passwordWallet.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Set;

@Getter
@Setter
@Entity
public class User extends IdModel {

    String login;
    String password_hash;
    String salt;
    Boolean isPasswordKeptAsHash;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private Set<Password> passwords;
}
