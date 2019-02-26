package red.medusa.miniblog.security_config.jwt;

import org.springframework.security.core.userdetails.User;

public class JwtUserFactory {

    private JwtUserFactory() {
    }

    public static JwtUser create(User user) {
        return new JwtUser(
                user.getUsername(),
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

}
