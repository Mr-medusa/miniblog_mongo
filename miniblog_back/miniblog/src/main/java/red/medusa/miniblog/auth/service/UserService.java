package red.medusa.miniblog.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import red.medusa.miniblog.security_config.bean.JwtAuthenticationResponse;
import red.medusa.miniblog.security_config.jwt.JwtTokenUtil;

@Service
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Qualifier("jwtUserDetailService")
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    public JwtAuthenticationResponse login(String username, String password) {

        UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);

        final Authentication authentication = authenticationManager.authenticate(upToken);

        SecurityContextHolder.getContext().setAuthentication(authentication);

        final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        final String token = jwtTokenUtil.generateToken(userDetails);

        String[] permissions = new String[authentication.getAuthorities().size()];

        Object[] objects = authentication.getAuthorities().toArray();

        for (int i = 0; i < objects.length; i++) {
            permissions[i] = ((GrantedAuthority) objects[i]).getAuthority();
        }

        return new JwtAuthenticationResponse(token, permissions);
    }

}
