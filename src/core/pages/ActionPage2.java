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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


public class ActionPage2 extends JSONPage {


    protected final String ACT_ADD_CAMP = "addCamp";
    protected final String ACT_DELETE_CAMP = "deleteCamp";
    protected final String ACT_UPDATE_CAMP = "updateCamp";
    protected final String ACT_ADD_TASK = "addTask";
    protected final String ACT_DELETE_TASK = "deleteTask";
    protected final String ACT_UPDATE_TASK = "updateTask";



    protected String paramAct;
    protected String paramCampType;
    protected String paramName;
    protected String paramSite;
    protected String paramPhone;
    protected String paramContacts;
    protected String paramAddNumber;
    protected String paramSource;
    protected String[] paramCampIds;
    protected String[] paramTaskIds;
    protected String paramPK;
    protected String paramAttrId;
    protected String paramValue;
    protected String paramComment;
    protected String paramDate;
    protected String paramMinute;
    protected String paramReason;
    protected String paramManagerId;
    protected String paramFio;
    protected String paramAddress;
    protected String paramScheme;
    protected String paramKm;
    protected String paramDateText;
    protected String paramBillNbr;
    

    public ActionPage2(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);

    }

    protected void authorize() throws Exception {
        if (getUser() == null) throw new RedirectException("/crm/login.jsp");
    }
    protected void executeAdd() throws Exception {
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        List vals;
        vals = new ArrayList();
        vals.add(paramName);
        params.put(new BigInteger(Attributes.NAME), vals);
        if (paramContacts != null) {
            vals = new ArrayList();
            vals.add(paramContacts);
            params.put(new BigInteger(Attributes.CONTACTS), vals);
        }
        if (paramAddress != null) {
            vals = new ArrayList();
            vals.add(paramAddress);
            params.put(new BigInteger(Attributes.ADDRESS), vals);
        }
        if (paramScheme != null) {
            vals = new ArrayList();
            vals.add(paramScheme);
            params.put(new BigInteger(Attributes.MAP), vals);
        }
        if (paramKm != null) {
            vals = new ArrayList();
            vals.add(paramKm);
            params.put(new BigInteger(Attributes.KM), vals);
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
        DBObject dbObject = DBHelper.createObject(ObjectTypes.Campaign2, "new camp", "", params);
        ObjectCache.addToCache(dbObject.getId(), dbObject);
        throw new RedirectException("/crm/campaigns.jsp");
    }
    protected void executeAddTask() throws Exception {
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        if (paramCampIds == null || paramCampIds.length == 0) throw new RedirectException("/crm/campaigns.jsp");
        String campId = paramCampIds[0];
        List vals;
        vals = new ArrayList();
        vals.add(new BigInteger(campId));
        params.put(new BigInteger(Attributes.CAMP_ID), vals);
        if (paramDateText != null) {
            vals = new ArrayList();
            vals.add(paramDateText);
            params.put(new BigInteger(Attributes.DATE_TEXT), vals);
        }
        if (paramComment != null) {
            vals = new ArrayList();
            vals.add(paramComment);
            params.put(new BigInteger(Attributes.COMMENT), vals);
        }
        if (paramBillNbr != null) {
            vals = new ArrayList();
            vals.add(paramBillNbr);
            params.put(new BigInteger(Attributes.BILL_NBR), vals);
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
        DBObject dbObject = DBHelper.createObject(ObjectTypes.DriverTask, "new task", "", params);
        ObjectCache.addToCache(dbObject.getId(), dbObject);
        throw new RedirectException("/crm/tasks.jsp");
    }
    protected void execute() throws Exception {
        if (ACT_ADD_CAMP.equals(paramAct)) {
            executeAdd();
        } else if (ACT_DELETE_CAMP.equals(paramAct)) {
            executeDelete();
        } else if (ACT_UPDATE_CAMP.equals(paramAct)) {
            executeUpdate();
        } else if (ACT_ADD_TASK.equals(paramAct)) {
            executeAddTask();
        } else if (ACT_DELETE_TASK.equals(paramAct)) {
            executeDeleteTask();
        } else if (ACT_UPDATE_TASK.equals(paramAct)) {
            executeUpdate();
        }
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
                Attributes.COMMENT.equals(paramAttrId) ||
                Attributes.BILL_NBR.equals(paramAttrId) ||
                Attributes.ADDRESS.equals(paramAttrId) ||
                Attributes.DATE_TEXT.equals(paramAttrId) ||
                Attributes.MAP.equals(paramAttrId) ||
                Attributes.KM.equals(paramAttrId) ||
                Attributes.FIO.equals(paramAttrId) ||
                Attributes.ADDITIONAL_NUMBER.equals(paramAttrId) ||
                Attributes.CALL_REASON.equals(paramAttrId) ||
                Attributes.SITE.equals(paramAttrId)) {
//            if (Attributes.SITE.equals(paramAttrId)) {
//                if (paramValue.isEmpty()) {
//                    putJSONError("Сайт не может быть пустым. ");
//                    return;
//                }
//                paramValue = TempHelper.cleanSite(paramValue);
//                if (TempHelper.siteOccurs(paramValue)) {
//                    putJSONError("Данный сайт уже существует в базе данных. ");
//                    return;
//                }
//            }
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
        if (paramCampIds == null || paramCampIds.length == 0) throw new RedirectException("/crm/campaigns.jsp");
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
        throw new RedirectException("/crm/campaigns.jsp");
    }

    protected void executeDeleteTask() throws Exception {
        if (paramTaskIds == null || paramTaskIds.length == 0) throw new RedirectException("/crm/tasks.jsp");
        for (String idStr: paramTaskIds) {
            try {
                if (idStr == null) continue;
                BigInteger idBig = new BigInteger(idStr);
                ObjectCache.addIdToLoad(idBig);
            } catch (NumberFormatException e) {
                Logger.error(e);
            }
        }
        for (String idStr: paramTaskIds) {
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
        throw new RedirectException("/crm/tasks.jsp");
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
        paramTaskIds = request.getParameterValues("taskId");
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
        paramFio = request.getParameter("fio");
        paramAddress = request.getParameter("address");
        paramScheme = request.getParameter("scheme");
        paramKm = request.getParameter("km");
        paramDateText = request.getParameter("dateText");
        paramBillNbr = request.getParameter("billNbr");

    }

}

