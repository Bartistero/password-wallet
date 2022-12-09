package pl.sterniczuk.passwordWallet.model;

import org.springframework.data.repository.CrudRepository;

public interface BlockIpAddressRepository extends CrudRepository<BlockIpAddress, Long> {

    BlockIpAddress findBlockIpAddressByIp(String ip);
}
