package core.pages;

import core.Logger;
import core.entity.User;
import core.exceptions.RedirectException;
import core.helpers.SQLController;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class AbstractPage {
    protected HttpServletRequest request;
    protected HttpServletResponse response;
    protected PrintWriter out;
    private String pageURL;

    public String getPageURL() {
        return pageURL;
    }

    public void setPageURL(String pageURL) {
        this.pageURL = pageURL;
    }

    protected User getUser() {
        return (User)request.getSession().getAttribute("user");
    }

    protected void removeUser() {
        request.getSession().removeAttribute("user");
    }

    protected void setUser(User user) {
        if (user == null) return;
        request.getSession().setAttribute("user", user);
    }
    protected void setUserMessage(String message) {
        if (message == null) return;
        request.getSession().setAttribute("message", message);
    }
    protected String getUserMessage() {
        return (String)request.getSession().getAttribute("message");
    }
    protected void removeUserMessage() {
        request.getSession().removeAttribute("message");
    }
    public AbstractPage(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");

        } catch (Exception e) {
            Logger.error(e);
        }
        try {
            out = response.getWriter();
        } catch (Exception e) {
            Logger.error(e);
        }
    }

    public void executeRequest() {
        try {
            parseParams();
            authorize();
            init();
            execute();
            render();
        } catch (RedirectException e) {
            try {
                response.sendRedirect(e.getUrl());
            } catch (Exception ex) {
                Logger.error(ex);
            }
        } catch (Exception e) {
            Logger.error(e);
            out.append("<!--\n");
            e.printStackTrace(out);
            out.append("-->");
        } finally {
            SQLController.closeConnection();
        }

    }

    protected void authorize() throws Exception {

    }

    protected void render() throws Exception {

    }

    protected void execute() throws Exception {

    }

    protected void init() throws Exception {

    }

    protected void parseParams() throws Exception {

    }
}
