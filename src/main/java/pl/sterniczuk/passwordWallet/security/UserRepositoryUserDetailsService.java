package pl.sterniczuk.passwordWallet.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.sterniczuk.passwordWallet.model.*;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalTime;
import java.util.List;

@Service
public class UserRepositoryUserDetailsService implements UserDetailsService {

    private UserRepository userRepo;
    private LoginHistoryRepository loginRepository;
    private HttpServletRequest request;
    private BlockIpAddressRepository ipRepo;

    @Autowired
    public UserRepositoryUserDetailsService(UserRepository userRepo, LoginHistoryRepository loginRepository, HttpServletRequest request, BlockIpAddressRepository ipRepo) {
        this.userRepo = userRepo;
        this.loginRepository = loginRepository;
        this.request = request;
        this.ipRepo = ipRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        Boolean block = user.getBlock();
        if (checkIp() == false) {
            throw new UsernameNotFoundException(
                    "User '" + username + "' not found");
        }
        if (user != null) {
            if (block) {
                if (LocalTime.now().isAfter(user.getBlockTime().plusSeconds(30))) {
                    user.setBlock(false);
                    user.setAttempt(0);
                    user.setBlockTime(null);
                    userRepo.save(user);
                    return user;
                } else {
                    throw new UsernameNotFoundException(
                            "User '" + username + "' not found");
                }
            }
            return user;
        }
        throw new UsernameNotFoundException(
                "User '" + username + "' not found");
    }

    private boolean checkIp() {
        String ip = getClientIP();
        BlockIpAddress block = ipRepo.findBlockIpAddressByIp(ip);
        if (block != null && LocalTime.now().isAfter(block.getData().plusSeconds(30))) {
            ipRepo.delete(block);
        } else {
            List<LoginHistory> historyOfLogin = loginRepository.getAllByIpAndType(ip, "Niepoprawne");
            if (historyOfLogin.size() >= 10) {
                BlockIpAddress newBlock = new BlockIpAddress(ip, LocalTime.now());
                if (ipRepo.findBlockIpAddressByIp(ip) == null) {
                    ipRepo.save(newBlock);
                }
                return false;
            }
        }
        return true;
    }

    private String getClientIP() {
        String xfHeader = request.getHeader("X-Forwarded-For");
        if (xfHeader == null) {
            return request.getRemoteAddr();
        }
        return xfHeader.split(",")[0];
    }
}