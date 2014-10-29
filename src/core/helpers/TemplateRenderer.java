package core.helpers;

import core.Logger;
import core.cache.ObjectCache;
import core.helpers.TemplateHelper;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class TemplateRenderer {
    protected Map<String, Object> templateParams;
    protected String templateString;
    protected BigInteger templateId;
    protected HttpServletRequest request;
    protected HttpServletResponse response;

    public TemplateRenderer(HttpServletRequest request, HttpServletResponse response) {
        this.request = request;
        this.response = response;
        try{
            request.setCharacterEncoding("UTF-8");
            response.setCharacterEncoding("UTF-8");
        } catch (Exception e) {
            Logger.error(e);
        }
        templateParams = new HashMap<String, Object>();
        templateParams.put("locale", new HashMap<String, String>());
    }
    public TemplateRenderer(HttpServletRequest request, HttpServletResponse response, BigInteger templateId) {
        this.request = request;
        this.response = response;
        this.templateId = templateId;
        ObjectCache.addIdToLoad(templateId);
        templateParams = new HashMap<String, Object>();
        templateParams.put("locale", new HashMap<String, String>());
    }
    protected Map<String, String> getLocales() {
        return (Map<String, String>)templateParams.get("locale");
    }
    public String render() throws Exception {
        initTemplateString();
        mapLabels();
        mapTemplateModel();
        return TemplateHelper.apply(templateString, templateParams);
    }
    protected void initTemplateString() {
        templateString = TemplateHelper.getTemplateString(templateId);
    }
    protected void mapLabels() throws Exception {

    }
    protected void mapTemplateModel() throws Exception {

    }
}
