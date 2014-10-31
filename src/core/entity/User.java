package core.entity;

import java.math.BigInteger;
import java.sql.Timestamp;

public class User {
    private BigInteger id;
    private String login;
    private String passHash;
    private Timestamp loginTime;
    private UserRoles role;

    public boolean isAdmin() {
        return UserRoles.Admin.equals(role);
    }

    public boolean isGuest() {
        return UserRoles.Guest.equals(role);
    }

    public void setRole(UserRoles role) {
        this.role = role;
    }

    public UserRoles getRole() {
        return role;
    }

    public BigInteger getId() {
        return id;
    }

    public void setId(BigInteger id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassHash() {
        return passHash;
    }

    public void setPassHash(String passHash) {
        this.passHash = passHash;
    }

    public Timestamp getLoginTime() {
        return loginTime;
    }

    public void setLoginTime(Timestamp loginTime) {
        this.loginTime = loginTime;
    }
}
