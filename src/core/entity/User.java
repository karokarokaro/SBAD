package core.entity;

import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.helpers.HashHelper;

import java.math.BigInteger;
import java.sql.Timestamp;

public class User {
    private BigInteger id;
    private String login;
    private String passHash;
    private Timestamp loginTime;
    private UserRoles role;
    private String patrname;
    private String name;
    private String surname;
    private String phone;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPatrname() {
        return patrname;
    }

    public void setPatrname(String patrname) {
        this.patrname = patrname;
    }

    public String getMiniInfo() {
        return "<div>" + (phone != null ? phone : "") + "</div>" +
                "<div class=\"telName\">"+(surname!=null ? surname+" " : "") +
                (name!=null ? name+" " : "") +
                (patrname!=null ? patrname : "") + "</div>";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

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
    
    public static User valueOf(DBObject userDB) {
        User user = new User();
        user.setId(userDB.getId());
        DBAttribute roleAttr = userDB.getAttributeById(Attributes.ROLE);
        UserRoles role = UserRoles.get(roleAttr.getTextValue());
        if (role != null) {
            user.setRole(role);
        } else {
            user.setRole(UserRoles.Guest);
        }
        user.setLogin(userDB.getAttributeById(Attributes.LOGIN).getTextValue());
        user.setPassHash(userDB.getAttributeById(Attributes.PASSWORD_HASH).getTextValue());
        user.setLoginTime(new Timestamp(1));
        DBAttribute attr;
        attr = userDB.getAttributeById(Attributes.NAME);
        if (attr!=null) user.setName(attr.getTextValue());
        attr = userDB.getAttributeById(Attributes.SURNAME);
        if (attr!=null) user.setSurname(attr.getTextValue());
        attr = userDB.getAttributeById(Attributes.PATR_NAME);
        if (attr!=null) user.setPatrname(attr.getTextValue());
        attr = userDB.getAttributeById(Attributes.PHONE);
        if (attr!=null) user.setPhone(attr.getTextValue());
        return user;
    }
}
