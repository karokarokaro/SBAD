package core.pages;

import core.entity.UserRoles;
import core.exceptions.RedirectException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RouterPage extends AbstractPage {

    public RouterPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/crm/login.jsp");
    }

    protected void execute() throws Exception {
        if (UserRoles.Manager.equals(getUser().getRole())) throw new RedirectException("/crm/tasks.jsp");
        if (UserRoles.Buyer.equals(getUser().getRole())) throw new RedirectException("/crm/tasks.jsp");
        throw new RedirectException("/crm/login.jsp");
    }
}
