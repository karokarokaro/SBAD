package core.pages;


import core.Logger;
import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.CampTypes;
import core.exceptions.RedirectException;
import core.helpers.TempHelper;
import core.helpers.TemplateRenderer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class MainPage extends HtmlPage {

    public MainPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    protected void init() throws Exception {
        super.init();
        addScriptFile("/js/inedit.js");
        setTitle("Главная страница | CRM");
    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/login.jsp");
    }

    protected void renderBody() throws Exception {
        TemplateRenderer body = new TemplateRenderer(request, response, new BigInteger("1415")) {
            protected void mapTemplateModel() throws Exception {
                Map user = new HashMap<String, String>();
                templateParams.put("user", user);
                user.put("login", getUser().getLogin());
                user.put("id", getUser().getId().toString());
                user.put("isAdmin", getUser().isAdmin());
                Map campTypes = new HashMap();
                templateParams.put("campTypes", campTypes);
                campTypes.put("oao", CampTypes.OAO.ordinal());
                campTypes.put("ooo", CampTypes.OOO.ordinal());
                campTypes.put("ip", CampTypes.IP.ordinal());
                List campaigns = new ArrayList();
                List managers = new ArrayList();
                templateParams.put("managers", managers);
                List<DBObject> managerList = TempHelper.getManagers();
                for (DBObject manag: managerList) {
                    if (manag.getId().equals(getUser().getId())) continue;
                    Map manager = new HashMap();
                    managers.add(manager);
                    manager.put("id", manag.getId().toString());
                    manager.put("name", manag.getAttributeById(Attributes.LOGIN).getTextValue());
                }
                templateParams.put("campaigns", campaigns);
                List<DBObject> camps = TempHelper.getCampaignsByUser(getUser());
                Collections.sort(camps, new Comparator<DBObject>() {
                    @Override
                    public int compare(DBObject o1, DBObject o2) {
                        Timestamp timestamp1 = o1.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        Timestamp timestamp2 = o2.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        return timestamp1.compareTo(timestamp2);
                    }
                });
                int i = 0;
                for (DBObject obj: camps) {
                    DBAttribute enabled = obj.getAttributeById(Attributes.ENABLED);
                    if (enabled != null && !enabled.getBooleanValue()) continue;
                    ++i;
                    Map campaign = new HashMap();
                    campaigns.add(campaign);
                    campaign.put("seqNumber", i);
                    campaign.put("id", obj.getId().toString());
                    DBAttribute callDate = obj.getAttributeById(Attributes.CALL_DATE);
                    String styleClass = "";
                    Calendar tomorrow = Calendar.getInstance();

                    if (callDate != null) {
                        if (!callDate.getTimestampValue().after(tomorrow.getTime())) {
                            styleClass = "danger";
                        } else {
                            tomorrow.set(Calendar.HOUR_OF_DAY, 23);
                            tomorrow.set(Calendar.MINUTE, 59);
                            if (!callDate.getTimestampValue().after(tomorrow.getTime())) {
                                styleClass = "success";
                            }
                        }

                    }
                    campaign.put("styleClass", styleClass);
                    int typ = obj.getAttributeById(Attributes.TYPE).getIntValue();
                    campaign.put("type", (typ >= 0 && typ < 3) ? CampTypes.values()[typ].toString() : "");
                    Map name = new HashMap();
                    campaign.put("name", name);
                    name.put("attrId", Attributes.NAME);
                    name.put("value", obj.getAttributeById(Attributes.NAME).getTextValue());
                    Map site = new HashMap();
                    campaign.put("site", site);
                    site.put("value", obj.getAttributeById(Attributes.SITE).getTextValue());
                    site.put("attrId", Attributes.SITE);
                    Map source = new HashMap();
                    campaign.put("source", source);
                    source.put("attrId", Attributes.SOURCE);
                    DBAttribute src = obj.getAttributeById(Attributes.SOURCE);
                    if (src != null) {
                        source.put("value", src.getTextValue());
                    } else {
                        source.put("value", "");
                    }
                    Map contacts = new HashMap();
                    campaign.put("contacts", contacts);
                    contacts.put("attrId", Attributes.CONTACTS);
                    DBAttribute cont = obj.getAttributeById(Attributes.CONTACTS);
                    if (cont != null) {
                        contacts.put("value", cont.getTextValue());
                    } else {
                        contacts.put("value", "");
                    }
                    Map phone = new HashMap();
                    campaign.put("phone", phone);
                    phone.put("attrId", Attributes.PHONE);

                    DBAttribute tel = obj.getAttributeById(Attributes.PHONE);
                    if (tel != null) {
                        phone.put("value", tel.getTextValue());
                    } else {
                        phone.put("value", "");
                    }
                    Map addNbr = new HashMap();
                    campaign.put("addNbr", addNbr);
                    addNbr.put("attrId", Attributes.ADDITIONAL_NUMBER);
                    DBAttribute addNumber = obj.getAttributeById(Attributes.ADDITIONAL_NUMBER);
                    if (addNumber != null) {
                        addNbr.put("value", addNumber.getTextValue());
                    } else {
                        addNbr.put("value", "");
                    }
                    Map result = new HashMap();
                    campaign.put("result", result);
                    result.put("attrId", Attributes.CALL_RESULTS);
                    List<DBAttribute> results = obj.getAttributesById(Attributes.CALL_RESULTS);
                    if (!results.isEmpty()) {
                        Collections.sort(results, new Comparator<DBAttribute>() {
                            @Override
                            public int compare(DBAttribute o1, DBAttribute o2) {
                                return -o1.getIdValue().compareTo(o2.getIdValue());
                            }
                        });
                        DBObject res = ObjectCache.getObject(results.get(0).getIdValue());
                        if (res != null) {
                            result.put("value", res.getAttributeById(Attributes.COMMENT).getTextValue());
                        }
                    } else {
                        result.put("value", "");
                    }
                    Map reason = new HashMap();
                    campaign.put("reason", reason);
                    reason.put("attrId", Attributes.CALL_REASON);
                    DBAttribute reas = obj.getAttributeById(Attributes.CALL_REASON);
                    if (reas != null) {
                        reason.put("value", reas.getTextValue());
                    } else {
                        reason.put("value", "");
                    }
                    Map date = new HashMap();
                    campaign.put("date", date);
                    date.put("attrId", Attributes.CALL_DATE);
                    DBAttribute dd = obj.getAttributeById(Attributes.CALL_DATE);
                    if (dd != null) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        date.put("value", dateFormat.format(dd.getTimestampValue()));
                    } else {
                        date.put("value", "");
                    }
                }

            }
        };
        out.append(body.render());
    }
}
