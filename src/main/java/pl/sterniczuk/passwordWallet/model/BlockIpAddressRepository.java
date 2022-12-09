package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface BlockIpAddressRepository extends CrudRepository<BlockIpAddress, Long> {

    List<BlockIpAddress> findBlockIpAddressByIp(String ip);
}
