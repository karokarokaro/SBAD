package core.pages;

import core.entity.HeaderTag;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlPage extends AbstractPage {
    private List<HeaderTag> headerTags;
    private List<String> statisticsStrings;

    public HtmlPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        headerTags = new ArrayList<HeaderTag>();
        statisticsStrings = new ArrayList<String>();
    }

    protected void init() throws Exception {
        addStyleSheetFile("/css/bootstrap.css");
        addStyleSheetFile("/css/bootstrap-modal-bs3patch.css");
        addStyleSheetFile("/css/bootstrap-modal.css");
        addStyleSheetFile("/css/bootstrap-formhelpers.min.css");
        addStyleSheetFile("/css/bootstrap-editable.css");
        addStyleSheetFile("/css/jquery.datetimepicker.css");
        addStyleSheetFile("/font-awesome/css/font-awesome.min.css");
        addStyleSheetFile("/css/admin.css");

        addScriptFile("/js/jquery.js");
        addScriptFile("/js/bootstrap.js");
        addScriptFile("/js/globals.js");
        addScriptFile("/js/bootstrap-modalmanager.js");
        addScriptFile("/js/bootstrap-modal.js");
        addScriptFile("/js/bootstrap-formhelpers.min.js");
        addScriptFile("/js/bootstrap-editable.js");
        addScriptFile("/js/editableCompName.js");
        addScriptFile("/js/jqBootstrapValidation.js");
        addScriptFile("/js/moment-with-langs.min.js");
        addScriptFile("/js/jquery.datetimepicker.js");
        addScriptFile("/js/bootstrap-datetimepicker.ru.js");
        addScriptFile("/js/admin.js");

        addMetaByHttpEquiv("Content-Type", "text/html; charset=utf-8");
        addMetaByName("viewport", "width=device-width, initial-scale=1.0");
    }

    public List<HeaderTag> getHeaderTags() {
        return headerTags;
    }

    public List<String> getStatisticsStrings() {
        return statisticsStrings;
    }

    public void addStatisticsString(String statString) {
        List<String> statisticsStrings = getStatisticsStrings();
        statisticsStrings.add(statString);
    }
    public void addMetaByName(String name, String content) {
        List<HeaderTag> headerTags = getHeaderTags();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("name", name);
        attributes.put("content", content);
        headerTags.add(new HeaderTag().setName("meta").setAttributes(attributes));
    }
    public void addMetaByHttpEquiv(String http_equiv, String content) {
        List<HeaderTag> headerTags = getHeaderTags();
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("http-equiv", http_equiv);
        attributes.put("content", content);
        headerTags.add(new HeaderTag().setName("meta").setAttributes(attributes));
    }
    public void addScriptFile(String fileName) {
        List<HeaderTag> headerTags = getHeaderTags();
        for (HeaderTag ht: headerTags) {
            if (ht.getAttributes().containsKey("src") && ht.getAttributes().get("src").equals(fileName)) return;
        }
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("type", "text/javascript");
        attributes.put("src", fileName);
        headerTags.add(new HeaderTag().setName("script").setAttributes(attributes).setValue(""));
    }
    public void addStyleSheetFile(String fileName) {
        List<HeaderTag> headerTags = getHeaderTags();
        for (HeaderTag ht: headerTags) {
            if (ht.getAttributes().containsKey("href") && ht.getAttributes().get("href").equals(fileName)) return;
        }
        Map<String, String> attributes = new HashMap<String, String>();
        attributes.put("rel", "stylesheet");
        attributes.put("href", fileName);
        attributes.put("type", "text/css");
        headerTags.add(new HeaderTag().setName("link").setAttributes(attributes));
    }
    public void setDescription(String description) {
        List<HeaderTag> headerTags = getHeaderTags();
        for (HeaderTag ht: headerTags) {
            if (ht.getAttributes().containsKey("name") && ht.getAttributes().get("name").equals("description")) headerTags.remove(ht);
        }
        addMetaByName("description", description);
    }
    public void setKeyWords(String keyWords) {
        List<HeaderTag> headerTags = getHeaderTags();
        for (HeaderTag ht: headerTags) {
            if (ht.getAttributes().containsKey("name") && ht.getAttributes().get("name").equals("keywords")) headerTags.remove(ht);
        }
        addMetaByName("keywords", keyWords);
    }
    public void setTitle(String title) {
        List<HeaderTag> headerTags = getHeaderTags();
        for (HeaderTag ht: headerTags) {
            if (ht.getName().equals("title")) headerTags.remove(ht);
        }
        HeaderTag ht = new HeaderTag();
        ht.setName("title");
        ht.setValue(title);
        Map<String, String> attributes = new HashMap<String, String>();
        ht.setAttributes(attributes);
        headerTags.add(ht);
    }
    protected void render() throws Exception {
        renderDocType();
        out.append("<html lang=\"ru\">\n");
        out.append("<head>\n");
        renderHeader();
        out.append("</head>\n");
        out.append("<body>\n");
        renderBody();
        out.append("</body>\n");
        out.append("</html>");
    }

    protected void renderBody() throws Exception {

    }

    protected void renderHeader() {
        if (headerTags != null) {
            for (HeaderTag headerTag: headerTags) {
                out.append("<").append(headerTag.getName());
                for (String attrName: headerTag.getAttributes().keySet()) {
                    out.append(" ").append(attrName).append("=\"");
                    out.append(headerTag.getAttributes().get(attrName)).append("\"");
                }
                if (headerTag.getValue() == null) {
                    out.append("/>\n");
                } else {
                    out.append(">");
                    out.append(headerTag.getValue());
                    out.append("</").append(headerTag.getName()).append(">\n");
                }
            }
        }
        if (statisticsStrings != null) {
            out.append("<!-- Statistics start -->");
            for (String statString: statisticsStrings) {
                out.append("\n"+statString+"\n");
            }
            out.append("<!-- Statistics end -->");
        }
    }

    protected void renderDocType() {
        out.append("<!DOCTYPE html>\n");
    }
}
