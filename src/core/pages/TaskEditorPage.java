package core.pages;


import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.CampTypes;
import core.exceptions.RedirectException;
import core.helpers.TempHelper;
import core.helpers.TemplateRenderer;
import sun.plugin.javascript.navig.Array;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class TaskEditorPage extends HtmlPage {

    public TaskEditorPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    protected void init() throws Exception {
        super.init();
        addScriptFile("/crm/js/admin.js");
        addScriptFile("/crm/js/inedit.js");
        setTitle("Редактор задач");
    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/crm/login.jsp");
    }


    protected void renderBody() throws Exception {
        TemplateRenderer body = new TemplateRenderer(request, response, new BigInteger("1415")) {
            protected void mapTemplateModel() throws Exception {
                DBAttribute attr;
                Map user = new HashMap<String, String>();
                templateParams.put("user", user);
                user.put("login", getUser().getLogin());
                user.put("id", getUser().getId().toString());
                user.put("isAdmin", getUser().isAdmin());
                user.put("isGuest", getUser().isGuest());
                user.put("roleDescr", getUser().getRole().getDescription());
                List tasks = new ArrayList();
                templateParams.put("tasks", tasks);
                List<DBObject> taskObjects = TempHelper.getTasksByUser(getUser());
                Collections.sort(taskObjects, new Comparator<DBObject>() {
                    @Override
                    public int compare(DBObject o1, DBObject o2) {
                        Timestamp timestamp1 = o1.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        Timestamp timestamp2 = o2.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        return timestamp1.compareTo(timestamp2);
                    }
                });
                int i = 0;
                for (DBObject obj: taskObjects) {
                    DBAttribute enabled = obj.getAttributeById(Attributes.ENABLED);
                    if (enabled != null && !enabled.getBooleanValue()) continue;
                    ++i;
                    Map task = new HashMap();
                    tasks.add(task);
                    task.put("seqNumber", i);
                    task.put("id", obj.getId().toString());
                    Map comment = new HashMap();
                    task.put("comment", comment);
                    comment.put("attrId", Attributes.COMMENT);
                    attr = obj.getAttributeById(Attributes.COMMENT);
                    if (attr != null) {
                        comment.put("value", attr.getTextValue());
                    } else {
                        comment.put("value", "");
                    }
                    Map date = new HashMap();
                    task.put("date", date);
                    date.put("attrId", Attributes.DATE);
                    DBAttribute dd = obj.getAttributeById(Attributes.DATE);
                    if (dd != null) {
                        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                        date.put("value", dateFormat.format(dd.getTimestampValue()));
                    } else {
                        date.put("value", "");
                    }
                }
                List campaigns = new ArrayList();
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
                for (DBObject obj: camps) {
                    DBAttribute enabled = obj.getAttributeById(Attributes.ENABLED);
                    if (enabled != null && !enabled.getBooleanValue()) continue;
                    Map campaign = new HashMap();
                    campaigns.add(campaign);
                    campaign.put("id", obj.getId().toString());
                    Map name = new HashMap();
                    campaign.put("name", name);
                    name.put("attrId", Attributes.NAME);
                    attr = obj.getAttributeById(Attributes.NAME);
                    if (attr != null) {
                        name.put("value", attr.getTextValue());
                    } else {
                        name.put("value", "");
                    }
                    Map address = new HashMap();
                    campaign.put("address", address);
                    address.put("attrId", Attributes.ADDRESS);
                    attr = obj.getAttributeById(Attributes.ADDRESS);
                    if (attr != null) {
                        address.put("value", attr.getTextValue());
                    } else {
                        address.put("value", "");
                    }
                    Map mapImg = new HashMap();
                    campaign.put("mapImg", mapImg);
                    mapImg.put("attrId", Attributes.MAP);
                    attr = obj.getAttributeById(Attributes.MAP);
                    if (attr != null) {
                        mapImg.put("value", attr.getTextValue());
                    } else {
                        mapImg.put("value", "");
                    }
                    Map km = new HashMap();
                    campaign.put("km", km);
                    km.put("attrId", Attributes.KM);
                    attr = obj.getAttributeById(Attributes.KM);
                    if (attr != null) {
                        km.put("value", attr.getTextValue());
                    } else {
                        km.put("value", "");
                    }
                    Map site = new HashMap();
                    campaign.put("site", site);
                    site.put("attrId", Attributes.SITE);
                    attr = obj.getAttributeById(Attributes.SITE);
                    if (attr != null) {
                        site.put("value", attr.getTextValue());
                    } else {
                        site.put("value", "");
                    }
                    Map contacts = new HashMap();
                    campaign.put("contacts", contacts);
                    contacts.put("attrId", Attributes.CONTACTS);
                    DBAttribute cont = obj.getAttributeById(Attributes.CONTACTS);
                    List conts = new ArrayList();
                    contacts.put("values", conts);
                    if (cont != null) {
                        String con = cont.getTextValue();
                        //add  to conts
                    }

                }


            }
        };
        out.append(body.render());
    }
}
