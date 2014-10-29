package core.cache;

import core.helpers.DBHelper;
import core.database.DBObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectTypeCache {
    private static Map<BigInteger, List<DBObject>> objects;
    private static List<BigInteger> idsToLoad;
    public static void reset() {
        objects = null;
    }
    public static void flush() {
        Map<BigInteger, List<DBObject>> objs = DBHelper.getObjectByTypeIds(idsToLoad);
        idsToLoad = null;
        for (BigInteger typeId: objs.keySet()) {
            addToCache(typeId, objs.get(typeId));
        }
    }
    public static void addIdToLoad(String id) {
        addIdToLoad(new BigInteger(id));
    }
    public static void addIdToLoad(BigInteger id) {
        if (objects != null && objects.containsKey(id)) return;
        if (idsToLoad == null) idsToLoad = new ArrayList<BigInteger>();
        if (!idsToLoad.contains(id)) idsToLoad.add(id);
    }
    public static List<DBObject> getObjectList(String id) {
        return getObjectList(new BigInteger(id));
    }
    public static List<DBObject> getObjectList(BigInteger id) {
        if (idsToLoad != null) flush();
        if (objects == null) {
            objects = new HashMap<BigInteger, List<DBObject>>();
        } else {
            if (objects.containsKey(id)) return objects.get(id);
        }
        List<DBObject> dbObjects = DBHelper.getObjectByTypeId(id);
        if (dbObjects == null) return null;
        for (DBObject object: dbObjects) {
            ObjectCache.addToCache(object.getId(), object);
        }
        addToCache(id, dbObjects);
        return dbObjects;
    }
    public static List<DBObject> addToCache(String id, List<DBObject> objectList) {
        return addToCache(new BigInteger(id), objectList);
    }
    public static List<DBObject> addToCache(BigInteger id, List<DBObject> objectList) {
        if (objects == null) objects = new HashMap<BigInteger, List<DBObject>>();
        List<DBObject> res = objects.get(id);
        objects.put(id, objectList);
        for (DBObject object: objectList) {
            ObjectCache.addToCache(object.getId(), object);
        }
        return res;
    }
}
