package core.pages;


import core.Logger;
import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.CampTypes;
import core.exceptions.RedirectException;
import core.helpers.TemplateRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.*;

public class HistoryRenderer extends HtmlPage {
    protected String paramCampId;

    public HistoryRenderer(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }
    protected void init() throws Exception {
        super.init();
        addScriptFile("/js/admin.js");
        addScriptFile("/js/inedit.js");
    }
    protected void parseParams() throws Exception {
        paramCampId = request.getParameter("campId");
    }
    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/login.jsp");
    }
    protected void render() throws Exception {
        TemplateRenderer history = new TemplateRenderer(request, response, new BigInteger("1448")) {
            protected void mapTemplateModel() throws Exception {
                DBObject object = ObjectCache.getObject(paramCampId);
                if (object == null) {
                    Logger.error("No object. object_id = " + paramCampId);
                } else {
                    List<DBAttribute> results = object.getAttributesById(Attributes.CALL_RESULTS);
                    Collections.sort(results, new Comparator<DBAttribute>() {
                        @Override
                        public int compare(DBAttribute o1, DBAttribute o2) {
                            return -o1.getIdValue().compareTo(o2.getIdValue());
                        }
                    });
                    for (DBAttribute attribute: results) {
                        ObjectCache.addIdToLoad(attribute.getIdValue());
                    }
                    Map campaign = new HashMap();
                    templateParams.put("campaign", campaign);
                    int typ = object.getAttributeById(Attributes.TYPE).getIntValue();
                    campaign.put("type", (typ >= 0 && typ < 3) ? CampTypes.values()[typ].toString() : "");
                    campaign.put("name", object.getAttributeById(Attributes.NAME).getTextValue());
                    List ress = new ArrayList();
                    templateParams.put("results", ress);
                    for (DBAttribute attribute: results) {
                        DBObject obj = ObjectCache.getObject(attribute.getIdValue());
                        if (obj == null) continue;
                        Map result = new HashMap();
                       String date = "";
                        DBAttribute ts = obj.getAttributeById(Attributes.DATE);
                        if (ts != null) {
                            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                            date = dateFormat.format(ts.getTimestampValue());
                        }
                        String comm = "";
                        DBAttribute cc = obj.getAttributeById(Attributes.COMMENT);
                        if (cc != null) {
                            comm = cc.getTextValue();
                        }
                        result.put("date", date);
                        result.put("text", comm);
                        String rea = "Без причины";
                        DBAttribute reas = obj.getAttributeById(Attributes.CALL_REASON);
                        if (reas != null) {
                            rea = reas.getTextValue();
                        }
                        result.put("date", date);
                        result.put("reason", rea);
                        result.put("text", comm);
                        ress.add(result);
                    }
                }
            }
        };
        out.append(history.render());
    }
}
