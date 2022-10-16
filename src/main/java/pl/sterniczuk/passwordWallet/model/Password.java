package pl.sterniczuk.passwordWallet.model;

import lombok.*;

import javax.persistence.*;


@Data
@Entity
@RequiredArgsConstructor
public class Password{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    String Password;
    String webAddress;
    String Description;
    String Login;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "idUser", nullable = false, insertable = false, updatable = false)
    private User user;
}
