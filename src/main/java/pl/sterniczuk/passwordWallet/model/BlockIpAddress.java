package pl.sterniczuk.passwordWallet.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalTime;

@Data
@Getter
@Setter
@Entity
public class BlockIpAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String ip;
    private LocalTime data;

    public BlockIpAddress(String ip, LocalTime data) {
        this.ip = ip;
        this.data = data;
    }

    public BlockIpAddress() {

    }
}
