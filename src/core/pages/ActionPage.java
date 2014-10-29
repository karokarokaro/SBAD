package core.pages;


import core.Logger;
import core.cache.ObjectCache;
import core.database.Attributes;
import core.database.DBAttribute;
import core.database.DBObject;
import core.database.ObjectTypes;
import core.exceptions.RedirectException;
import core.helpers.DBHelper;
import core.helpers.TempHelper;
import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ActionPage extends JSONPage {

    private final String ADMIN_ID = "4473";

    protected final String ACT_ADD_CAMP = "addCamp";
    protected final String ACT_DELETE_CAMP = "deleteCamp";
    protected final String ACT_UPDATE_CAMP = "updateCamp";
    protected final String ACT_CALL = "call";
    protected final String ACT_REJECT_CAMP = "rejectCamp";
    protected final String ACT_TRANSFER_CAMP = "transferCamp";



    protected String paramAct;
    protected String paramCampType;
    protected String paramName;
    protected String paramSite;
    protected String paramPhone;
    protected String paramContacts;
    protected String paramAddNumber;
    protected String paramSource;
    protected String[] paramCampIds;
    protected String paramPK;
    protected String paramAttrId;
    protected String paramValue;
    protected String paramComment;
    protected String paramDate;
    protected String paramMinute;
    protected String paramReason;
    protected String paramManagerId;

    public ActionPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);

    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/login.jsp");
    }
    protected void executeAdd() throws Exception {
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        List vals = new ArrayList();
        vals.add(Integer.valueOf(paramCampType));
        params.put(new BigInteger(Attributes.TYPE), vals);
        vals = new ArrayList();
        vals.add(paramName);
        params.put(new BigInteger(Attributes.NAME), vals);
        if (paramSite == null) {
            Logger.error("paramSite is empty");
            throw new RedirectException("/");
        } else if (TempHelper.siteOccurs(paramSite)) {
            setUserMessage("Данный сайт уже зарегистрирован в системе. ");
            throw new RedirectException("/");
        }
        vals = new ArrayList();
        vals.add(paramSite);
        params.put(new BigInteger(Attributes.SITE), vals);
        if (paramPhone != null) {
            vals = new ArrayList();
            vals.add(paramPhone);
            params.put(new BigInteger(Attributes.PHONE), vals);
        }
        if (paramAddNumber != null) {
            vals = new ArrayList();
            vals.add(paramAddNumber);
            params.put(new BigInteger(Attributes.ADDITIONAL_NUMBER), vals);
        }
        if (paramContacts != null) {
            vals = new ArrayList();
            vals.add(paramContacts);
            params.put(new BigInteger(Attributes.CONTACTS), vals);
        }
        if (paramSource != null) {
            vals = new ArrayList();
            vals.add(paramSource);
            params.put(new BigInteger(Attributes.SOURCE), vals);
        }
        vals = new ArrayList();
        vals.add(getUser().getId());
        params.put(new BigInteger(Attributes.CREATED_BY), vals);
        vals = new ArrayList();
        vals.add("now()");
        params.put(new BigInteger(Attributes.CREATED_WHEN), vals);
        vals = new ArrayList();
        vals.add(Integer.valueOf(1));
        params.put(new BigInteger(Attributes.ENABLED), vals);
        DBObject dbObject = DBHelper.createObject(ObjectTypes.Campaign, "new camp", "", params);
        ObjectCache.addToCache(dbObject.getId(), dbObject);
        throw new RedirectException("/");
    }
    protected void execute() throws Exception {
        if (ACT_ADD_CAMP.equals(paramAct)) {
            executeAdd();
        } else if (ACT_DELETE_CAMP.equals(paramAct)) {
            executeDelete();
        } else if (ACT_UPDATE_CAMP.equals(paramAct)) {
            executeUpdate();
        } else if (ACT_CALL.equals(paramAct)) {
            executeCall();
        } else if (ACT_REJECT_CAMP.equals(paramAct)) {
            executeReject();
        } else if (ACT_TRANSFER_CAMP.equals(paramAct)) {
            executeTransfer();
        }
    }

    protected void executeReject() throws Exception {
        if (paramCampIds == null || paramCampIds.length == 0) throw new RedirectException("/");
        for (String idStr: paramCampIds) {
            try {
                if (idStr == null) continue;
                BigInteger idBig = new BigInteger(idStr);
                ObjectCache.addIdToLoad(idBig);
            } catch (NumberFormatException e) {
                Logger.error(e);
            }
        }
        for (String idStr: paramCampIds) {
            if (idStr == null) continue;
            transferCamp(idStr, ADMIN_ID);
        }
        putJSONSuccess();
        throw new RedirectException("/");
    }

    protected void executeTransfer() throws Exception {
        if (paramCampIds == null || paramCampIds.length == 0) throw new RedirectException("/");
        if (paramManagerId == null) throw new RedirectException("/");
        if (!getUser().isAdmin()) throw new RedirectException("/");
        for (String idStr: paramCampIds) {
            try {
                if (idStr == null) continue;
                BigInteger idBig = new BigInteger(idStr);
                ObjectCache.addIdToLoad(idBig);
            } catch (NumberFormatException e) {
                Logger.error(e);
            }
        }
        for (String idStr: paramCampIds) {
            if (idStr == null) continue;
            transferCamp(idStr, paramManagerId);
        }
        putJSONSuccess();
        throw new RedirectException("/");
    }

    protected void transferCamp(String campId, String managerId) throws Exception {
        BigInteger idBig = new BigInteger(campId);
        BigInteger managerIdBig = new BigInteger(managerId);
        DBObject object = ObjectCache.getObject(idBig);
        if (object == null) {
            Logger.error("Objects is already deleted. object_id = " + campId);
            return;
        }
        if (!getUser().getId().equals(object.getAttributeById(Attributes.CREATED_BY).getIdValue())) {
            return;
        }
        object.getAttributeById(Attributes.CREATED_BY).setIdValue(managerIdBig);
        DBHelper.updateObject(object);
    }

    protected void executeCall() throws Exception {
        if (paramCampIds == null || paramCampIds[0] == null) return;
        String idStr = paramCampIds[0];
        BigInteger idBig = new BigInteger(idStr);
        DBObject object = ObjectCache.getObject(idBig);
        if (object == null) {
            Logger.error("Objects is already deleted. object_id = " + idStr);
            return;
        }
        if (!getUser().getId().equals(object.getAttributeById(Attributes.CREATED_BY).getIdValue())) {
            return;
        }
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        List vals;
        vals = new ArrayList();
        vals.add("now()");
        params.put(new BigInteger(Attributes.DATE), vals);
        vals = new ArrayList();
        vals.add((paramComment != null && !paramComment.isEmpty()) ? paramComment : "Без коментариев");
        params.put(new BigInteger(Attributes.COMMENT), vals);
        DBAttribute reas = object.getAttributeById(Attributes.CALL_REASON);
        String rea = "Без причины";
        if (reas != null && reas.getTextValue() != null) {
            rea = reas.getTextValue();
        }
        vals = new ArrayList();
        vals.add(rea);
        params.put(new BigInteger(Attributes.CALL_REASON), vals);
        DBObject dbObject = DBHelper.createObject(ObjectTypes.CallResult, "new result", "", params);
        ObjectCache.addToCache(dbObject.getId(), dbObject);

        try {
            DBAttribute result = new DBAttribute(new BigInteger(Attributes.CALL_RESULTS));
            result.setValueTypeUsing("id");
            result.setIdValue(dbObject.getId());
            object.getAttributes().add(result);
            DBAttribute attrDate = object.getAttributeById(Attributes.CALL_DATE);
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = null;
            if (paramDate != null && !paramDate.isEmpty()) {
                if (paramMinute == null || paramMinute.isEmpty()) paramMinute = "00";
                try {
                    date = dateFormat.parse(paramDate + ":" + paramMinute);
                } catch (ParseException e) {
                    Logger.error("incorrect date: " + paramDate);
                }
            }
            if (date != null) {
                if (attrDate == null) {
                    attrDate = new DBAttribute(new BigInteger(Attributes.CALL_DATE));
                    attrDate.setValueTypeUsing("ts");
                    object.getAttributes().add(attrDate);
                }
                Timestamp ts = new Timestamp(date.getTime());
                if (ts == null) {
                    putJSONError("Ошибка формата. ");
                    Logger.error("Ошибка формата даты: " + paramValue);
                } else {
                    attrDate.setTimestampValue(ts);
                }
            } else {
                if (attrDate != null) {
                    object.getAttributes().remove(attrDate);
                }
            }
            if (paramReason != null && !paramReason.isEmpty()) {
                if (reas == null) {
                    reas = new DBAttribute(new BigInteger(Attributes.CALL_REASON));
                    reas.setValueTypeUsing("text");
                    object.getAttributes().add(reas);
                }
                reas.setTextValue(paramReason);
            } else {
                if (reas != null) {
                    object.getAttributes().remove(reas);
                }
            }
            DBHelper.updateObject(object);
        } catch (NumberFormatException e) {
            Logger.error(e);
        }
        putJSONSuccess();
        throw new RedirectException("/");
    }

    protected void executeUpdate() throws Exception {
        BigInteger objId = null;
        DBObject object = null;
        try{
            objId = new BigInteger(paramPK);
            object = ObjectCache.getObject(objId);
            if (object == null) {
                putJSONError("Объект удален. ");
                Logger.error("Objects is already deleted. object_id = " + paramPK);
                return;
            }
        } catch (NumberFormatException e) {
            putJSONError("Объект удален. ");
            Logger.error(e);
        }
        if (Attributes.SOURCE.equals(paramAttrId) ||
                Attributes.NAME.endsWith(paramAttrId) ||
                Attributes.CONTACTS.equals(paramAttrId) ||
                Attributes.PHONE.equals(paramAttrId) ||
                Attributes.ADDITIONAL_NUMBER.equals(paramAttrId) ||
                Attributes.CALL_REASON.equals(paramAttrId) ||
                Attributes.SITE.equals(paramAttrId)) {
            if (Attributes.SITE.equals(paramAttrId)) {
                if (paramValue.isEmpty()) {
                    putJSONError("Сайт не может быть пустым. ");
                    return;
                }
                paramValue = TempHelper.cleanSite(paramValue);
                if (TempHelper.siteOccurs(paramValue)) {
                    putJSONError("Данный сайт уже существует в базе данных. ");
                    return;
                }
            }
            DBAttribute attr = object.getAttributeById(paramAttrId);
            if (attr == null) {
                attr = new DBAttribute(new BigInteger(paramAttrId));
                attr.setValueTypeUsing("text");
                object.getAttributes().add(attr);
            }
            attr.setTextValue(paramValue);
            DBHelper.updateObject(object);
            putJSONSuccess();
            putJSONUpdateInfo();
        } else if (Attributes.CALL_DATE.equals(paramAttrId)) {
            if (paramValue.isEmpty()) {
                DBAttribute attr = object.getAttributeById(paramAttrId);
                if (attr != null) {
                    object.getAttributes().remove(attr);
                }
                DBHelper.updateObject(object);
                putJSONSuccess();
                putJSONUpdateInfo();
                return;
            }
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = dateFormat.parse(paramValue);
            Timestamp ts = new Timestamp(date.getTime());
            if (ts == null) {
                putJSONError("Ошибка формата. ");
                Logger.error("Ошибка формата даты: " + paramValue);
            } else {
                DBAttribute attr = object.getAttributeById(paramAttrId);
                if (attr == null) {
                    attr = new DBAttribute(new BigInteger(paramAttrId));
                    attr.setValueTypeUsing("ts");
                    object.getAttributes().add(attr);
                }
                attr.setTimestampValue(ts);
                DBHelper.updateObject(object);
                putJSONSuccess();
                putJSONUpdateInfo();
            }
        }
    }

    protected void putJSONUpdateInfo() {
        outObject.put("obj-id", Integer.valueOf(paramPK).intValue());
        outObject.put("attr-id", Integer.valueOf(paramAttrId).intValue());
    }

    protected void executeDelete() throws Exception {
        if (paramCampIds == null || paramCampIds.length == 0) throw new RedirectException("/");
        for (String idStr: paramCampIds) {
            try {
                if (idStr == null) continue;
                BigInteger idBig = new BigInteger(idStr);
                ObjectCache.addIdToLoad(idBig);
            } catch (NumberFormatException e) {
                Logger.error(e);
            }
        }
        for (String idStr: paramCampIds) {
            try {
                if (idStr == null) continue;
                BigInteger idBig = new BigInteger(idStr);
                DBObject object = ObjectCache.getObject(idBig);
                if (object == null) {
                    Logger.error("Objects is already deleted. object_id = " + idStr);
                    continue;
                }
                if (!getUser().getId().equals(object.getAttributeById(Attributes.CREATED_BY).getIdValue())) {
                    continue;
                }
                DBAttribute enabledAttr = object.getAttributeById(Attributes.ENABLED);
                if (enabledAttr == null) {
                    enabledAttr = new DBAttribute(new BigInteger(Attributes.ENABLED));
                    enabledAttr.setValueTypeUsing("double");
                    object.getAttributes().add(enabledAttr);
                }
                enabledAttr.setDoubleValue(0);
                DBHelper.updateObject(object);
            } catch (NumberFormatException e) {
                Logger.error(e);
            }
        }
        throw new RedirectException("/");
    }

    protected void parseParams() throws Exception {
        paramAct = request.getParameter("act");
        paramCampType = request.getParameter("campType");
        paramName = request.getParameter("name");
        paramSite = request.getParameter("site");
        if (paramSite != null) {
            paramSite = TempHelper.cleanSite(paramSite);
        }
        paramPhone = request.getParameter("phone");
        paramSource = request.getParameter("source");
        paramCampIds = request.getParameterValues("campId");
        paramPK = request.getParameter("pk");
        paramValue = request.getParameter("value");
        paramAttrId = request.getParameter("attrId");
        paramComment = request.getParameter("comment");
        paramDate = request.getParameter("date");
        paramMinute = request.getParameter("minute");
        paramReason = request.getParameter("reason");
        paramContacts = request.getParameter("contacts");
        paramAddNumber = request.getParameter("addNumber");
        paramManagerId = request.getParameter("managerId");
    }

}
