package core.pages;

import core.Logger;
import core.entity.User;
import core.entity.UserRoles;
import core.exceptions.RedirectException;
import core.helpers.SQLController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class RouterPage extends AbstractPage {

    public RouterPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/login.jsp");
    }

    protected void execute() throws Exception {
        AbstractPage page = null;
        if (UserRoles.Manager.equals(getUser().getRole())) page = new ManagerPage(request, response);
        if (UserRoles.Buyer.equals(getUser().getRole())) page = new ManagerPage(request, response);

        if (page != null) {
            page.executeRequest();
        } else {
            throw new RedirectException("/crm/login.jsp");
        }
    }
}
