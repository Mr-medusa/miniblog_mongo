package red.medusa.miniblog.auth.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import red.medusa.miniblog.auth.service.UserService;
import red.medusa.miniblog.security_config.bean.JwtAuthenticationRequest;
import red.medusa.miniblog.security_config.bean.JwtAuthenticationResponse;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * miniUser 登陆
     * @param authenticationRequest
     * @return
     * @throws AuthenticationException
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody JwtAuthenticationRequest authenticationRequest) throws AuthenticationException {

        JwtAuthenticationResponse res = userService.login(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        return ResponseEntity.ok(res);
    }

}
