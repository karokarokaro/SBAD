package core.pages;

import core.database.Attributes;
import core.helpers.TempHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ValidatorPage extends JSONPage {

    protected String paramName;
    protected String paramValue;

    public ValidatorPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    protected void parseParams() throws Exception {
        paramName = request.getParameter("field");
        paramValue = request.getParameter("value");
    }
    protected void validate() throws Exception {
        if ("site".equals(paramName)) {
            paramValue = TempHelper.cleanSite(paramValue);
            boolean isValid = !TempHelper.siteOccurs(paramValue);
            outObject.put("valid", isValid);
            if (!isValid) {
                outObject.put("message", "Данный сайт уже зарегистрирован в системе. ");
            }
        }
    }
    protected void execute() throws Exception {
        outObject.put("value", paramValue);
        validate();
    }
}
