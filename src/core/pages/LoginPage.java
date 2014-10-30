package core.pages;

import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.User;
import core.entity.UserRoles;
import core.entity.search.SearchParams;
import core.exceptions.RedirectException;
import core.helpers.HashHelper;
import core.helpers.TempHelper;
import core.helpers.TemplateRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;

public class LoginPage extends HtmlPage {
    protected String paramLogin;
    protected String paramPassword;
    protected String paramAct;

    protected final String ACT_LOGIN = "login";
    protected final String ACT_LOGOUT = "logout";


    protected String message;

    protected void init() throws Exception {
        super.init();
        //addScriptFile("/js/admin.js");
        //addScriptFile("/js/inedit.js");
    }

    public LoginPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        setTitle("Добро пожаловать | CRM");
    }

    protected void parseParams() throws Exception {
        paramLogin = request.getParameter("login");
        paramPassword = request.getParameter("pass");
        paramAct = request.getParameter("act");
    }

    protected void authorize() throws Exception {
        //if (getUser() != null) throw new RedirectException("/");
    }

    protected void renderBody() throws Exception {
        TemplateRenderer loginBody = new TemplateRenderer(request, response, new BigInteger("1413")) {
            protected void mapTemplateModel() throws Exception {
                templateParams.put("message", message != null ? message : "");
            }
        };
        out.append(loginBody.render());
    }

    protected void execute() throws Exception {
        if (ACT_LOGIN.equals(paramAct)) {
            if (paramLogin != null && paramPassword != null) {
                SearchParams params = new SearchParams();
                DBObject userDB = TempHelper.getUserByLogin(paramLogin);
               // out.append(userDB == null?"Pusto":"ne pusto");
                if (userDB == null || !HashHelper.getPasswordHash(paramPassword)
                        .equals(userDB.getAttributeById(Attributes.PASSWORD_HASH).getTextValue())) {
                    message = "Логин или пароль введены неверно.";
                } else {
                    User user = new User();
                    user.setId(userDB.getId());
                    DBAttribute roleAttr = userDB.getAttributeById(Attributes.ROLE);
                    user.setRole(UserRoles.get(roleAttr.getTextValue()));
                    user.setLogin(paramLogin);
                    user.setPassHash(HashHelper.getPasswordHash(paramPassword));
                    user.setLoginTime(new Timestamp(1));
                    setUser(user);
                    throw new RedirectException("/");
                }
            }
        } else if (ACT_LOGOUT.equals(paramAct)) {
            if(getUser() != null) {
                removeUser();
                throw new RedirectException("/login.jsp");
            }
        }


    }

}
