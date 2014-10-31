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
        if (getUser() == null) throw new RedirectException("/login.jsp");
    }

    protected void execute() throws Exception {
        AbstractPage page = null;
        if (UserRoles.Manager.equals(getUser().getRole())) page = new CampEditorPage(request, response);
        if (UserRoles.Buyer.equals(getUser().getRole())) page = new CampEditorPage(request, response);

        if (page != null) {
            page.executeRequest();
        } else {
            throw new RedirectException("/crm/login.jsp");
        }
    }
}
