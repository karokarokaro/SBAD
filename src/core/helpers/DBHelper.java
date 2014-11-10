package core.helpers;

import core.Logger;
import core.cache.ObjectCache;
import core.database.DBAttribute;
import core.database.DBObject;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 14.08.13
 * Time: 10:55
 * To change this template use File | Settings | File Templates.
 */
public class DBHelper {
    private static final String SQL_GET_OBJECT_BY_ID = "select o.name as objName, \n" +
            "o.description as objDescription, \n" +
            "ot.object_type_id as typeId, \n" +
            "ot.name as typeName, \n" +
            "ot.description as typeDescription, \n" +
            "a.attr_id as attrId, \n" +
            "a.name as attrName, \n" +
            "a.description as attrDescription, \n" +
            "vt.value_type_id as valueTypeId, \n" +
            "vt.name as valueTypeName, \n" +
            "vt.using as valueTypeUsing, \n" +
            "v.value_id as valueId, \n" +
            "v.double_v as doubleValue, \n" +
            "v.ts_v as timestampValue, \n" +
            "v.id_v as idValue, \n" +
            "v.text_v as textValue\n" +
            "from (select * from objects where object_id={id}) o\n" +
            "join object_types ot on o.object_type_id=ot.object_type_id\n" +
            "left join vals li on ot.attr_list_id=li.list_id\n" +
            "left join attributes a on li.id_v=a.attr_id\n" +
            "left join value_types vt on a.value_type_id=vt.value_type_id\n" +
            "left join vals v on v.object_id=o.object_id and v.attr_id=a.attr_id\n" +
            "order by o.object_id, li.order;";
    private static final String SQL_GET_OBJECT_BY_TYPE_ID = "select o.object_id as id, o.name as objName, \n" +
            "o.description as objDescription, \n" +
            "ot.object_type_id as typeId, \n" +
            "ot.name as typeName, \n" +
            "ot.description as typeDescription, \n" +
            "a.attr_id as attrId, \n" +
            "a.name as attrName, \n" +
            "a.description as attrDescription, \n" +
            "vt.value_type_id as valueTypeId, \n" +
            "vt.name as valueTypeName, \n" +
            "vt.using as valueTypeUsing, \n" +
            "v.value_id as valueId, \n" +
            "v.double_v as doubleValue, \n" +
            "v.ts_v as timestampValue, \n" +
            "v.id_v as idValue, \n" +
            "v.text_v as textValue\n" +
            "from (select * from objects where object_type_id={object_type_id}) o\n" +
            "join object_types ot on o.object_type_id=ot.object_type_id\n" +
            "left join vals li on ot.attr_list_id=li.list_id\n" +
            "left join attributes a on li.id_v=a.attr_id\n" +
            "left join value_types vt on a.value_type_id=vt.value_type_id\n" +
            "left join vals v on v.object_id=o.object_id and v.attr_id=a.attr_id\n" +
            "order by o.object_id, li.order;";
    private static final String SQL_GET_OBJECT_BY_TYPE_IDS = "select o.object_id as id, o.name as objName, \n" +
            "o.description as objDescription, \n" +
            "ot.object_type_id as typeId, \n" +
            "ot.name as typeName, \n" +
            "ot.description as typeDescription, \n" +
            "a.attr_id as attrId, \n" +
            "a.name as attrName, \n" +
            "a.description as attrDescription, \n" +
            "vt.value_type_id as valueTypeId, \n" +
            "vt.name as valueTypeName, \n" +
            "vt.using as valueTypeUsing, \n" +
            "v.value_id as valueId, \n" +
            "v.double_v as doubleValue, \n" +
            "v.ts_v as timestampValue, \n" +
            "v.id_v as idValue, \n" +
            "v.text_v as textValue\n" +
            "from (select * from objects where object_type_id in {object_type_id}) o\n" +
            "join object_types ot on o.object_type_id=ot.object_type_id\n" +
            "left join vals li on ot.attr_list_id=li.list_id\n" +
            "left join attributes a on li.id_v=a.attr_id\n" +
            "left join value_types vt on a.value_type_id=vt.value_type_id\n" +
            "left join vals v on v.object_id=o.object_id and v.attr_id=a.attr_id\n" +
            "order by o.object_type_id, o.object_id, li.order;";
    private static final String SQL_GET_OBJECT_BY_IDS = "select o.object_id as id, o.name as objName, \n" +
            "o.description as objDescription, \n" +
            "ot.object_type_id as typeId, \n" +
            "ot.name as typeName, \n" +
            "ot.description as typeDescription, \n" +
            "a.attr_id as attrId, \n" +
            "a.name as attrName, \n" +
            "a.description as attrDescription, \n" +
            "vt.value_type_id as valueTypeId, \n" +
            "vt.name as valueTypeName, \n" +
            "vt.using as valueTypeUsing, \n" +
            "v.value_id as valueId, \n" +
            "v.double_v as doubleValue, \n" +
            "v.ts_v as timestampValue, \n" +
            "v.id_v as idValue, \n" +
            "v.text_v as textValue\n" +
            "from (select * from objects where object_id in {ids}) o\n" +
            "join object_types ot on o.object_type_id=ot.object_type_id\n" +
            "left join vals li on ot.attr_list_id=li.list_id\n" +
            "left join attributes a on li.id_v=a.attr_id\n" +
            "left join value_types vt on a.value_type_id=vt.value_type_id\n" +
            "left join vals v on v.object_id=o.object_id and v.attr_id=a.attr_id\n" +
            "order by o.object_id, li.order;";
    private static final String SQL_GET_ATTRIBUTE_VALUE_TEXT = "select text_v as val from vals " +
            "where object_id={objId} and attr_id={attr_id}";
    private static final String SQL_DELETE_VALUE = "delete from vals where value_id = {id}";
    private static final String SQL_GET_IDS_BY_TYPE = "select object_id as id from objects o where object_type_id={objTypeId}";
    private static final String SQL_CONDITION_OBJ_ATTR_ID_VALUE = " and exists(select * from vals v where o.object_id=v.object_id and v.attr_id={attr_id} and id_v={value})";
    private static final String SQL_CONDITION_OBJ_ATTR_TEXT_VALUE_EQ = " and exists(select * from vals v where o.object_id=v.object_id and v.attr_id={attr_id} and text_v = '{value}')";
    private static final String SQL_CONDITION_OBJ_ATTR_TS_VALUE_EQ = " and exists(select * from vals v where o.object_id=v.object_id and v.attr_id={attr_id} and ts_v = '{value}')";
    private static final String SQL_CONDITION_OBJ_ATTR_DOUBLE_VALUE_EQ = " and exists(select * from vals v where o.object_id=v.object_id and v.attr_id={attr_id} and double_v = {value})";
    private static final String SQL_GET_LIST_ITEMS = "select vt.value_type_id as typeId, \n" +
            "vt.name as typeName, \n" +
            "vt.using as typeUsing, \n" +
            "v.value_id as valueId, \n" +
            "v.double_v as doubleValue, \n" +
            "v.id_v as idValue, \n" +
            "v.text_v as textValue, \n" +
            "v.ts_v as tsValue \n" +
            "from (select * from lists where list_id={id}) l\n" +
            "join value_types vt on l.value_type_id=vt.value_type_id\n" +
            "join vals v on v.list_id=l.list_id\n" +
            "order by v.order";
    private static final String SQL_GET_LOCALES = "select * from locale l \n" +
            "where {cond} \n" +
            "order by l.lang_id, l.locale_id;";
    private static final String SQL_GET_LOCALES_CONDITION = " l.lang_id = {langId} and l.locale_id in ({locIds}) ";


    public static String getTextAttributeValueSafe(BigInteger objectId, BigInteger attributeId) {
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_ATTRIBUTE_VALUE_TEXT.replace("{objId}", objectId.toString())
                    .replace("{attr_id}", attributeId.toString()));
            if (resultSet.next()) {
                return resultSet.getString("val");
            } else {
                return "";
            }
        } catch (Exception e) {
            Logger.error(e);
            return "";
        }
    }
    private static List<DBObject> getObjectFromResultSet(ResultSet resultSet) throws Exception {
        List<DBObject> res = new ArrayList<DBObject>();
        if (resultSet.next()) {
            String curId = "-1";
            DBObject curObj = null;
            BigDecimal tmpDec;
            do {
                if (!curId.equals(resultSet.getBigDecimal("id").toBigInteger().toString())) {
                    if (curObj != null) res.add(curObj);
                    curId = resultSet.getBigDecimal("id").toBigInteger().toString();
                    curObj = new DBObject(new BigInteger(curId)).setName(resultSet.getString("objName"))
                            .setDescription(resultSet.getString("objDescription"))
                            .setTypeId(resultSet.getBigDecimal("typeID").toBigInteger())
                            .setTypeName(resultSet.getString("typeName"))
                            .setTypeDescription(resultSet.getString("typeDescription"));
                }
                if ((tmpDec = resultSet.getBigDecimal("attrId")) != null) {
                    DBAttribute dbAttribute = new DBAttribute(tmpDec.toBigInteger());
                    dbAttribute.setName(resultSet.getString("attrName"))
                            .setDescription(resultSet.getString("attrDescription"))
                            .setDoubleValue(resultSet.getDouble("doubleValue"))
                            .setValueTypeId((tmpDec = resultSet.getBigDecimal("valueTypeId")) != null ? tmpDec.toBigInteger() : null)
                            .setValueId((tmpDec = resultSet.getBigDecimal("valueId")) != null ? tmpDec.toBigInteger() : null)
                            .setValueTypeName(resultSet.getString("valueTypeName"))
                            .setValueTypeUsing(resultSet.getString("valueTypeUsing"))
                            .setIdValue((tmpDec = resultSet.getBigDecimal("idValue")) != null ? tmpDec.toBigInteger() : null)
                            .setTextValue(resultSet.getString("textValue"))
                            .setTimestampValue(resultSet.getTimestamp("timestampValue"));
                    if ("text".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getTextValue() != null ||
                            "id".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getIdValue() != null ||
                            "ts".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getTimestampValue() != null ||
                            "double".equals(dbAttribute.getValueTypeUsing())) {
                        curObj.getAttributes().add(dbAttribute);
                    }

                }
            } while (resultSet.next());
            if (curObj != null) res.add(curObj);
        }
        return res;
    }
    public static List<DBObject> getObjectByIdsQuery(String idsQuery) {
        List<DBObject> res = new ArrayList<DBObject>();
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_OBJECT_BY_IDS.replace("{ids}", "(" + idsQuery + ")"));
            res = getObjectFromResultSet(resultSet);
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            return res;
        }
    }
    private static String getIdListQuery(List<BigInteger> ids) {
        List<DBObject> res = new ArrayList<DBObject>();
        if (ids.size() == 0) return null;
        StringBuilder idsStr = new StringBuilder();
        idsStr.append("("+ids.get(0).toString());
        for (int i = 1; i < ids.size(); ++i) {
            idsStr.append(", "+ids.get(i).toString());
        }
        idsStr.append(")");
        return idsStr.toString();
    }
    public static List<DBObject> getObjectByIds(List<BigInteger> ids) {
        String idsStr = getIdListQuery(ids);
        List<DBObject> res = new ArrayList<DBObject>();
        if (idsStr == null) return res;
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_OBJECT_BY_IDS.replace("{ids}", idsStr.toString()));
            res = getObjectFromResultSet(resultSet);
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            return res;
        }
    }
    public static DBObject getObjectById(BigInteger id) {
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_OBJECT_BY_ID.replace("{id}", id.toString()));
            if (resultSet.next()) {
                DBObject resObject = new DBObject(id);
                resObject.setName(resultSet.getString("objName"))
                        .setDescription(resultSet.getString("objDescription"))
                        .setTypeId(resultSet.getBigDecimal("typeID").toBigInteger())
                        .setTypeName(resultSet.getString("typeName"))
                        .setTypeDescription(resultSet.getString("typeDescription"));
                BigDecimal tmpDec;
                do {
                    if ((tmpDec = resultSet.getBigDecimal("attrId")) != null) {
                        DBAttribute dbAttribute = new DBAttribute(tmpDec.toBigInteger());
                        dbAttribute.setName(resultSet.getString("attrName"))
                                .setDescription(resultSet.getString("attrDescription"))
                                .setDoubleValue(resultSet.getDouble("doubleValue"))
                                .setValueTypeId((tmpDec = resultSet.getBigDecimal("valueTypeId")) != null ? tmpDec.toBigInteger() : null)
                                .setValueId((tmpDec = resultSet.getBigDecimal("valueId")) != null ? tmpDec.toBigInteger() : null)
                                .setValueTypeName(resultSet.getString("valueTypeName"))
                                .setValueTypeUsing(resultSet.getString("valueTypeUsing"))
                                .setIdValue((tmpDec = resultSet.getBigDecimal("idValue")) != null ? tmpDec.toBigInteger() : null)
                                .setTextValue(resultSet.getString("textValue"))
                                .setTimestampValue(resultSet.getTimestamp("timestampValue"));
                        if ("text".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getTextValue() != null ||
                                "id".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getIdValue() != null ||
                                "ts".equals(dbAttribute.getValueTypeUsing()) && dbAttribute.getTimestampValue() != null ||
                                "double".equals(dbAttribute.getValueTypeUsing())) {
                            resObject.getAttributes().add(dbAttribute);
                        }

                    }
                } while (resultSet.next());
                return resObject;
            } else {
                return null;
            }
        } catch (Exception e) {
            Logger.error(e);
            return null;
        }
    }
    public static DBObject getObjectById(String id) {
        return getObjectById(new BigInteger(id));
    }
    public static List<DBObject> getObjectByTypeId(String typeId) {
        List<DBObject> res = new ArrayList<DBObject>();
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_OBJECT_BY_TYPE_ID.replace("{object_type_id}", typeId));
            res = getObjectFromResultSet(resultSet);
        } catch (Exception e) {
            Logger.error(e);
        }
        return res;
    }
    public static Map<BigInteger, List<DBObject>> getObjectByTypeIds(List<BigInteger> ids) {
        String idsStr = getIdListQuery(ids);
        List<DBObject> resTemp = new ArrayList<DBObject>();
        Map<BigInteger, List<DBObject>> res = new HashMap<BigInteger, List<DBObject>>();
        if (idsStr == null) return res;
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_OBJECT_BY_TYPE_IDS.replace("{ids}", idsStr.toString()));
            resTemp = getObjectFromResultSet(resultSet);
            for (DBObject dbObject: resTemp) {
                if (!res.containsKey(dbObject.getTypeId())) res.put(dbObject.getTypeId(), new ArrayList<DBObject>());
                res.get(dbObject.getTypeId()).add(dbObject);
            }
        } catch (Exception e) {
            Logger.error(e);
        } finally {
            return res;
        }
    }
    public static List<DBObject> getObjectByTypeId(BigInteger typeId) {
        return getObjectByTypeId(typeId.toString());
    }
    public static List<DBAttribute> getListById(BigInteger listId) {
        List<DBAttribute> res = new ArrayList<DBAttribute>();
        try{
            BigDecimal tmpDec;
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_LIST_ITEMS.replace("{id}", listId.toString()));
            while (resultSet.next()) {
                DBAttribute dbAttribute = new DBAttribute(listId);
                dbAttribute.setValueTypeId((tmpDec = resultSet.getBigDecimal("typeId")) != null ? tmpDec.toBigInteger() : null)
                        .setValueId((tmpDec = resultSet.getBigDecimal("valueId")) != null ? tmpDec.toBigInteger() : null)
                        .setValueTypeName(resultSet.getString("typeName"))
                        .setValueTypeUsing(resultSet.getString("typeUsing"));
                dbAttribute.setDoubleValue(resultSet.getDouble("doubleValue"))
                        .setTextValue(resultSet.getString("textValue"))
                        .setTimestampValue(resultSet.getTimestamp("tsValue"))
                        .setIdValue((tmpDec = resultSet.getBigDecimal("idValue")) != null ? tmpDec.toBigInteger() : null);
                res.add(dbAttribute);
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return res;
    }
    public static List<DBAttribute> getListById(String listId) {
        return getListById(new BigInteger(listId));
    }
    private static void deleteValue(String valueId) {
        deleteValue(new BigInteger(valueId));
    }
    private static boolean deleteValue(BigInteger valueId) {
        try {
            SQLController.executeUpdate(SQL_DELETE_VALUE.replace("{id}", valueId.toString()));
            return true;
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    private static boolean insertValue(BigInteger objId, DBAttribute attr) {
        String using = attr.getValueTypeUsing();
        String fieldName;
        if (using.equals("id")) {
            fieldName = "id_v";
        } else if (using.equals("ts")) {
            fieldName = "ts_v";
        } else if (using.equals("text")) {
            fieldName = "text_v";
        } else {
            fieldName = "double_v";
        }
        try{
            PreparedStatement preparedStatement = SQLController.prepare("INSERT INTO vals (`object_id`, `attr_id`, `"+fieldName+"`) " +
                    "VALUES (" + objId.toString() + ", " + attr.getId().toString() + ", ?)");
            if (using.equals("id")) {
                preparedStatement.setBigDecimal(1, new BigDecimal(attr.getIdValue()));
            } else if (using.equals("ts")) {
                preparedStatement.setTimestamp(1, attr.getTimestampValue());
            } else if (using.equals("text")) {
                preparedStatement.setString(1, attr.getTextValue());
            } else {
                preparedStatement.setDouble(1, attr.getDoubleValue());
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    private static boolean updateValue(DBAttribute attr) {
        String using = attr.getValueTypeUsing();
        String fieldName;
        if (using.equals("id")) {
            fieldName = "id_v";
        } else if (using.equals("ts")) {
            fieldName = "ts_v";
        } else if (using.equals("text")) {
            fieldName = "text_v";
        } else {
            fieldName = "double_v";
        }
        try{
            PreparedStatement preparedStatement = SQLController.prepare("update vals set "+fieldName+" = ?"+" where value_id="+attr.getValueId().toString());
            if (using.equals("id")) {
                preparedStatement.setBigDecimal(1, new BigDecimal(attr.getIdValue()));
            } else if (using.equals("ts")) {
                preparedStatement.setTimestamp(1, attr.getTimestampValue());
            } else if (using.equals("text")) {
                preparedStatement.setString(1, attr.getTextValue());
            } else {
                preparedStatement.setDouble(1, attr.getDoubleValue());
            }
            return preparedStatement.executeUpdate() > 0;
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    public static boolean updateObject(DBObject newObject) {
        BigInteger id = newObject.getId();
        ObjectCache.removeFromCache(id);
        DBObject dbObject = getObjectById(id);
        if (dbObject == null) {
            Logger.error("No object to update. objectId = " + id.toString());
            return false;
        }
        boolean res = true;
        for (DBAttribute dbAttribute: dbObject.getAttributes()) {
            DBAttribute attr = newObject.getAttributeByValueId(dbAttribute.getValueId());
            if (attr == null) {
                res &= deleteValue(dbAttribute.getValueId());
            } else {
                if (!attr.equals(dbAttribute)) {
                    res &= updateValue(attr);
                }
                newObject.getAttributes().remove(attr);
            }
        }
        for (DBAttribute attribute: newObject.getAttributes()) {
            if (dbObject.getAttributeByValueId(attribute.getValueId()) == null) {
                res &= insertValue(newObject.getId(), attribute);
            }
        }
        return res;
    }

    public static DBObject createObject(BigInteger objTypeId, String name, String description, Map<BigInteger, List> params) {
        return createObject(objTypeId.toString(), name, description, params);
    }
    public static DBObject createObject(String objTypeId, String name, String description, Map<BigInteger, List> params) {
        String objId = null;
        try{
            int n = SQLController.executeUpdate("INSERT INTO objects (`name`, `description`, `object_type_id`) VALUES (" +
                    "'"+name+"', " +
                    "'"+description+"', " +
                    objTypeId+");");
            if (n == 0) return null;
            ResultSet resultSet = SQLController.executeSelect("select object_id from objects order by object_id desc;");
            resultSet.beforeFirst(); resultSet.next();
            objId = resultSet.getBigDecimal("object_id").toBigInteger().toString();
            for (BigInteger attrId: params.keySet()) {
                if (params.get(attrId).size() == 0) continue;
                String fieldName;
                resultSet = SQLController.executeSelect("select vt.using as us from attributes a " +
                        "join value_types vt on a.value_type_id=vt.value_type_id " +
                        "where a.attr_id="+attrId.toString());
                if (resultSet.next()) {
                    String using = resultSet.getString("us");
                    if (using.equals("id")) {
                        fieldName = "id_v";
                    } else if (using.equals("ts")) {
                        fieldName = "ts_v";
                    } else if (using.equals("text")) {
                        fieldName = "text_v";
                    } else {
                        fieldName = "double_v";
                    }
                    StringBuilder values = new StringBuilder();
                    List list = params.get(attrId);
                    for (int i = 0; i < list.size(); ++i) {
                        if ("now()".equals(list.get(i))) {
                            values.append((i>0?",":"")+"("+objId+","+attrId.toString()+",now())");
                        } else {
                            values.append((i>0?",":"")+"("+objId+","+attrId.toString()+",?)");
                        }
                    }
                    PreparedStatement preparedStatement = SQLController.prepare("INSERT INTO vals (`object_id`, `attr_id`, `"+fieldName+"`) " +
                            "VALUES "+ values.toString());
                    for (int i = 0; i < list.size(); ++i) {
                        Object val = list.get(i);
                        if (using.equals("id")) {
                            if (val instanceof String) {
                                preparedStatement.setBigDecimal(i+1, new BigDecimal((String)val));
                            } else {
                                BigInteger tmp = (BigInteger)val;
                                preparedStatement.setBigDecimal(i+1, new BigDecimal(tmp));
                            }
                        } else if (using.equals("ts")) {
                            if ("now()".equals(val)) {
                                continue;
                            } else if (val instanceof String) {
                                preparedStatement.setTimestamp(i+1, Timestamp.valueOf((String) val));
                            } else {
                                preparedStatement.setTimestamp(i+1, (Timestamp) val);
                            }
                        } else if (using.equals("text")) {
                            preparedStatement.setString(i+1, (String)val);
                        } else {
                            if (val instanceof String) {
                                preparedStatement.setDouble(i+1, Double.valueOf((String)val));
                            }else if (val instanceof Integer) {
                                preparedStatement.setInt(i+1, (Integer)val);
                            } else {
                                preparedStatement.setDouble(i+1, (Double)val);
                            }
                        }
                    }
                    n = preparedStatement.executeUpdate();
                    if (n < list.size()) return null;
                }
            }
            return getObjectById(objId);
        } catch (Exception e) {
            if (objId != null) deleteObject(objId);
            Logger.error(e);
            return null;
        }
    }
    public static boolean deleteObject(String objId) {
        try{
            int n;
            n = SQLController.executeUpdate("delete from vals where object_id="+objId);
            n = SQLController.executeUpdate("delete from objects where object_id="+objId);
            if(n == 0) return false;
            return true;
        } catch (Exception e) {
            Logger.error(e);
            return false;
        }
    }
    public static boolean deleteObject(BigInteger objId) {
        return deleteObject(objId.toString());
    }
    public static Map<BigInteger, Map<BigInteger, String>> getLocales(Map<BigInteger, List<BigInteger>> ids) {
        StringBuilder condition = new StringBuilder();
        Map<BigInteger, Map<BigInteger, String>> res = new HashMap<BigInteger, Map<BigInteger, String>>();
        for (BigInteger lang: ids.keySet()) {
            StringBuilder locs = new StringBuilder();
            for (BigInteger locId: ids.get(lang)) {
                if (locs.length() > 0) locs.append(",");
                locs.append(locId.toString());
            }
            if (condition.length() > 0) condition.append(" or ");
            condition.append(SQL_GET_LOCALES_CONDITION.replace("{langId}", lang.toString()).replace("{locIds}", locs.toString()));
        }
        String sql = SQL_GET_LOCALES.replace("{cond}", condition.toString());
        try{
            BigDecimal tmpDec;
            ResultSet resultSet = SQLController.executeSelect(sql);
            while (resultSet.next()) {
                BigInteger langId = (tmpDec = resultSet.getBigDecimal("lang_id")) != null ? tmpDec.toBigInteger() : null;
                BigInteger locId = (tmpDec = resultSet.getBigDecimal("locale_id")) != null ? tmpDec.toBigInteger() : null;
                String localeText = resultSet.getString("text");
                if (langId != null && locId != null && localeText != null) {
                    if (!res.containsKey(langId)) res.put(langId, new HashMap<BigInteger, String>());
                    res.get(langId).put(locId, localeText);
                }
            }
        } catch (Exception e) {
            Logger.error(e);
        }
        return res;
    }
}
