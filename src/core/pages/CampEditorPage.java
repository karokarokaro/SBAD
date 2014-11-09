package core.pages;


import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.entity.CampTypes;
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

public class CampEditorPage extends HtmlPage {

    public CampEditorPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
    }

    protected void init() throws Exception {
        super.init();
        addScriptFile("/crm/js/admin.js");
        addScriptFile("/crm/js/inedit.js");
        setTitle("Редактор компаний");
    }

    protected void authorize() throws Exception {
        if (getUser() == null
                || !(UserRoles.Buyer.equals(getUser().getRole()) ||
                    UserRoles.Manager.equals(getUser().getRole()))) throw new RedirectException("/crm/login.jsp");
    }


    protected void renderBody() throws Exception {
        TemplateRenderer body = new TemplateRenderer(request, response, new BigInteger("4786")) {
            protected void mapTemplateModel() throws Exception {
                Map user = new HashMap<String, String>();
                templateParams.put("user", user);
                user.put("login", getUser().getLogin());
                user.put("id", getUser().getId().toString());
                user.put("fullName", getUser().getFullName());
                user.put("isAdmin", getUser().isAdmin());
                user.put("isGuest", getUser().isGuest());
                user.put("roleDescr", getUser().getRole().getDescription());
                List campaigns = new ArrayList();
                templateParams.put("campaigns", campaigns);
                List<DBObject> camps = TempHelper.getCampaigns2ByUser(getUser());
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
                    Map name = new HashMap();
                    campaign.put("name", name);
                    name.put("attrId", Attributes.NAME);
                    name.put("value", obj.getAttributeById(Attributes.NAME).getTextValue());
                    Map address = new HashMap();
                    campaign.put("address", address);
                    address.put("attrId", Attributes.ADDRESS);
                    if (obj.getAttributeById(Attributes.ADDRESS) != null) {
                        address.put("value", obj.getAttributeById(Attributes.ADDRESS).getTextValue());
                    } else {
                        address.put("value", "");
                    }
                    Map mapImg = new HashMap();
                    campaign.put("scheme", mapImg);
                    mapImg.put("attrId", Attributes.MAP);
                    if (obj.getAttributeById(Attributes.MAP) != null &&
                            obj.getAttributeById(Attributes.MAP).getTextValue().length() > 0) {
                        mapImg.put("value", obj.getAttributeById(Attributes.MAP).getTextValue());
                        mapImg.put("uploaded", true);
                    } else {
                        mapImg.put("value", "");
                        mapImg.put("uploaded", false);
                    }
                    Map km = new HashMap();
                    campaign.put("km", km);
                    km.put("attrId", Attributes.KM);
                    if (obj.getAttributeById(Attributes.KM) != null) {
                        km.put("value", obj.getAttributeById(Attributes.KM).getTextValue());
                    } else {
                        km.put("value", "");
                    }
//                    Map fio = new HashMap();
//                    campaign.put("fio", fio);
//                    fio.put("attrId", Attributes.FIO);
//                    if (obj.getAttributeById(Attributes.FIO) != null) {
//                        fio.put("value", obj.getAttributeById(Attributes.FIO).getTextValue());
//                    } else {
//                        fio.put("value", "");
//                    }
                    Map contacts = new HashMap();
                    campaign.put("contacts", contacts);
                    contacts.put("attrId", Attributes.CONTACTS);
                    DBAttribute cont = obj.getAttributeById(Attributes.CONTACTS);
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
