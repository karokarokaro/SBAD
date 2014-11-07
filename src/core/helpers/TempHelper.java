package core.helpers;

import core.Logger;
import core.cache.ObjectCache;
import core.cache.ObjectTypeCache;
import core.database.Attributes;
import core.database.DBObject;
import core.database.ObjectTypes;
import core.entity.User;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class TempHelper {
    public static DBObject getUserByLogin(String login) {
        PreparedStatement statement;
        BigInteger id;
        try {
            statement = SQLController.prepare("select object_id from vals where text_v = ?;");
            statement.setString(1, login);
            ResultSet resultSet = statement.executeQuery();
            resultSet.beforeFirst();
            if (resultSet.next()) {
                id = resultSet.getBigDecimal("object_id").toBigInteger();
                return ObjectCache.getObject(id);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return null;
    }
    public static List<DBObject> getManagers() {
        List<DBObject> res = ObjectTypeCache.getObjectList(ObjectTypes.User);
        if (res != null) {
            Collections.sort(res, new Comparator<DBObject>() {
                @Override
                public int compare(DBObject o1, DBObject o2) {
                    return o1.getAttributeById(Attributes.LOGIN).getTextValue()
                            .compareTo(o2.getAttributeById(Attributes.LOGIN).getTextValue());
                }
            });
        }
        return res;
    }

    public static List<DBObject> getCampaignsByUser(User user) {
        List<BigInteger> ids = new ArrayList<BigInteger>();
        try {
            ResultSet resultSet = SQLController.executeSelect("select object_id from vals where attr_id=" +
                    Attributes.CREATED_BY + " and id_v = " + user.getId().toString());
            while (resultSet.next()) {
                BigInteger id = resultSet.getBigDecimal("object_id").toBigInteger();
                ids.add(id);
                ObjectCache.addIdToLoad(id);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        List<DBObject> res = new ArrayList<DBObject>();
        for (BigInteger id: ids) {
            DBObject dbObject = ObjectCache.getObject(id);
            if (dbObject != null) {
                res.add(dbObject);
            }
        }
        return res;
    }
    public static List<DBObject> getCampaigns2ByUser(User user) {
        List<BigInteger> ids = new ArrayList<BigInteger>();
        try {
            ResultSet resultSet = SQLController.executeSelect("select object_id from vals v where v.attr_id=" +
                    Attributes.CREATED_BY + " and v.id_v = " + user.getId().toString() +
                    " and exists(select object_id from objects o where o.object_id=v.object_id and " +
                    "o.object_type_id="+ObjectTypes.Campaign2+")");
            while (resultSet.next()) {
                BigInteger id = resultSet.getBigDecimal("object_id").toBigInteger();
                ids.add(id);
                ObjectCache.addIdToLoad(id);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        List<DBObject> res = new ArrayList<DBObject>();
        for (BigInteger id: ids) {
            DBObject dbObject = ObjectCache.getObject(id);
            if (dbObject != null) {
                res.add(dbObject);
            }
        }
        return res;
    }
    public static List<DBObject> getTasksByUser(User user) {
        List<BigInteger> ids = new ArrayList<BigInteger>();
        try {
            ResultSet resultSet = SQLController.executeSelect("select object_id from vals v where v.attr_id=" +
                    Attributes.CREATED_BY + " and v.id_v = " + user.getId().toString() +
                    " and exists(select object_id from objects o where o.object_id=v.object_id and " +
                    "o.object_type_id="+ObjectTypes.DriverTask+")");
            while (resultSet.next()) {
                BigInteger id = resultSet.getBigDecimal("object_id").toBigInteger();
                ids.add(id);
                ObjectCache.addIdToLoad(id);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        List<DBObject> res = new ArrayList<DBObject>();
        for (BigInteger id: ids) {
            DBObject dbObject = ObjectCache.getObject(id);
            if (dbObject != null) {
                res.add(dbObject);
            }
        }
        return res;
    }
    public static boolean siteOccurs(String site) {
        PreparedStatement ps;
        try{
            ps = SQLController.prepare("select object_id from vals v where " +
                    "attr_id = " + Attributes.SITE + " and text_v like ? && not exists " +
                    "(select object_id from vals vv where vv.object_id=v.object_id and attr_id="+Attributes.ENABLED+" and vv.double_v = 0)");
            ps.setString(1, site);
            ResultSet resultSet = ps.executeQuery();
            resultSet.beforeFirst();
            return resultSet.next();
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }

    public static String cleanSite(String site) {
        site = site.trim();
        if (site.startsWith("http://")) {
            site = site.substring(7);
        }
        if (site.startsWith("https://")) {
            site = site.substring(8);
        }
        site = site.trim();
        if (site.charAt(site.length()-1) == '/') {
            site = site.substring(0, site.length()-1);
        }
        return site;
    }
}
