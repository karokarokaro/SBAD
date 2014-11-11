package core.pages;


import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.UserRoles;
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
        if (getUser() == null
                || !(UserRoles.Buyer.equals(getUser().getRole()) ||
                UserRoles.Manager.equals(getUser().getRole()))) throw new RedirectException("/crm/login.jsp");
    }


    protected void renderBody() throws Exception {
        TemplateRenderer body = new TemplateRenderer(request, response, new BigInteger("4784")) {
            protected void mapTemplateModel() throws Exception {
                DBAttribute attr;
                Map user = new HashMap<String, String>();
                templateParams.put("user", user);
                user.put("login", getUser().getLogin());
                user.put("id", getUser().getId().toString());
                user.put("fullName", getUser().getMiniInfo());
                user.put("isAdmin", getUser().isAdmin());
                user.put("isGuest", getUser().isGuest());
                user.put("roleDescr", getUser().getRole().getDescription());
                List campaigns = new ArrayList();
                templateParams.put("campaigns", campaigns);
                List<DBObject> campObjects = TempHelper.getCampaigns2ByUser(getUser());
                Collections.sort(campObjects, new Comparator<DBObject>() {
                    @Override
                    public int compare(DBObject o1, DBObject o2) {
                        Timestamp timestamp1 = o1.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        Timestamp timestamp2 = o2.getAttributeById(Attributes.CREATED_WHEN).getTimestampValue();
                        return timestamp1.compareTo(timestamp2);
                    }
                });
                for (DBObject obj: campObjects) {
                    DBAttribute enabled = obj.getAttributeById(Attributes.ENABLED);
                    if (enabled != null && !enabled.getBooleanValue()) continue;
                    Map camp = new HashMap();
                    campaigns.add(camp);
                    camp.put("id", obj.getId().toString());
                    if (obj.getAttributeById(Attributes.NAME) != null) {
                        camp.put("name",obj.getAttributeById(Attributes.NAME).getTextValue());
                    } else {
                        camp.put("name","");
                    }


                }
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
                    ObjectCache.addIdToLoad(obj.getAttributeById(Attributes.CAMP_ID).getIdValue());
                }
                for (DBObject obj: taskObjects) {
                    DBAttribute enabled = obj.getAttributeById(Attributes.ENABLED);
                    if (enabled != null && !enabled.getBooleanValue()) continue;
                    ++i;
                    Map task = new HashMap();
                    tasks.add(task);
                    task.put("seqNumber", i);
                    task.put("id", obj.getId().toString());
                    Map comment = new HashMap();
                    task.put("comments", comment);
                    comment.put("attrId", Attributes.COMMENT);
                    attr = obj.getAttributeById(Attributes.COMMENT);
                    if (attr != null) {
                        comment.put("value", attr.getTextValue());
                    } else {
                        comment.put("value", "");
                    }
                    Map billNbr = new HashMap();
                    task.put("billNbr", billNbr);
                    billNbr.put("attrId", Attributes.BILL_NBR);
                    attr = obj.getAttributeById(Attributes.BILL_NBR);
                    if (attr != null) {
                        billNbr.put("value", attr.getTextValue());
                    } else {
                        billNbr.put("value", "");
                    }
                    Map date = new HashMap();
                    task.put("dateText", date);
                    date.put("attrId", Attributes.DATE_TEXT);
                    attr = obj.getAttributeById(Attributes.DATE_TEXT);
                    if (attr != null) {
                        date.put("value", attr.getTextValue());
                    } else {
                        date.put("value", "");
                    }
                    task.put("userFullName", getUser().getMiniInfo());
                    Map dateOnly = new HashMap();
                    task.put("dateOnly", dateOnly);
                    dateOnly.put("attrId", Attributes.DATE_ONLY);
                    DBAttribute dd = obj.getAttributeById(Attributes.DATE_ONLY);
                    if (dd != null) {
                        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
                        dateOnly.put("value", dateFormat.format(dd.getTimestampValue()));
                    } else {
                        dateOnly.put("value", "");
                    }
                    Map campaign = new HashMap();
                    task.put("campaign", campaign);
                    DBObject obj1 = ObjectCache.getObject(obj.getAttributeById(Attributes.CAMP_ID).getIdValue());
                    campaign.put("id", obj1.getId().toString());
                    Map name = new HashMap();
                    campaign.put("name", name);
                    name.put("attrId", Attributes.NAME);
                    if (obj1.getAttributeById(Attributes.NAME) != null) {
                        name.put("value", obj1.getAttributeById(Attributes.NAME).getTextValue());
                    } else {
                        name.put("value", "");
                    }
                    Map address = new HashMap();
                    campaign.put("address", address);
                    address.put("attrId", Attributes.ADDRESS);
                    if (obj1.getAttributeById(Attributes.ADDRESS) != null) {
                        address.put("value", obj1.getAttributeById(Attributes.ADDRESS).getTextValue());
                    } else {
                        address.put("value", "");
                    }
                    Map mapImg = new HashMap();
                    campaign.put("scheme", mapImg);
                    mapImg.put("attrId", Attributes.MAP);
                    if (obj1.getAttributeById(Attributes.MAP) != null &&
                            obj1.getAttributeById(Attributes.MAP).getTextValue().length() > 0) {
                        mapImg.put("value", obj1.getAttributeById(Attributes.MAP).getTextValue());
                        mapImg.put("uploaded", true);
                    } else {
                        mapImg.put("value", "");
                        mapImg.put("uploaded", false);
                    }
                    Map km = new HashMap();
                    campaign.put("km", km);
                    km.put("attrId", Attributes.KM);
                    if (obj1.getAttributeById(Attributes.KM) != null) {
                        km.put("value", obj1.getAttributeById(Attributes.KM).getTextValue());
                    } else {
                        km.put("value", "");
                    }
//                    Map fio = new HashMap();
//                    campaign.put("fio", fio);
//                    fio.put("attrId", Attributes.FIO);
//                    if (obj1.getAttributeById(Attributes.FIO) != null) {
//                        fio.put("value", obj1.getAttributeById(Attributes.FIO).getTextValue());
//                    } else {
//                        fio.put("value", "");
//                    }
                    Map contacts = new HashMap();
                    campaign.put("contacts", contacts);
                    contacts.put("attrId", Attributes.CONTACTS);
                    DBAttribute cont = obj1.getAttributeById(Attributes.CONTACTS);
                    if (cont != null) {
                        contacts.put("value", cont.getTextValue());
                    } else {
                        contacts.put("value", "");
                    }
                }
            }
        };
        out.append(body.render());
    }
}
