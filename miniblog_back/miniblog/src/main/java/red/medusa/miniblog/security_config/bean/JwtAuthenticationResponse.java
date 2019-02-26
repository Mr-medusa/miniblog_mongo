package red.medusa.miniblog.security_config.bean;

public class JwtAuthenticationResponse{

    private final String token;

    private String[] permissions;

    public JwtAuthenticationResponse(String token,String[] permissions) {
        this.token = token;
        this.permissions = permissions;
    }

    public String getToken() {
        return this.token;
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }
}
