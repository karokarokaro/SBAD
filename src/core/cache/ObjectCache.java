package core.cache;

import core.helpers.DBHelper;
import core.database.DBObject;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ObjectCache {
    private static Map<BigInteger, DBObject> objects;
    private static List<BigInteger> idsToLoad;
    public static void reset() {
        objects = null;
    }
    private static void flush() {
        List<DBObject> objs = DBHelper.getObjectByIds(idsToLoad);
        idsToLoad = null;
        for (DBObject dbObject: objs) {
            addToCache(dbObject.getId().toString(), dbObject);
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
    public static DBObject getObject(String id) {
        return getObject(new BigInteger(id));
    }
    public static DBObject getObject(BigInteger id) {
        if (idsToLoad != null) flush();
        if (objects == null) {
            objects = new HashMap<BigInteger, DBObject>();
        } else {
            if (objects.containsKey(id)) return objects.get(id);
        }
        DBObject dbObject = DBHelper.getObjectById(id);
        if (dbObject == null) return null;
        objects.put(id, dbObject);
        return dbObject;
    }
    public static DBObject addToCache(String id, DBObject object) {
        return addToCache(new BigInteger(id), object);
    }
    public static DBObject addToCache(BigInteger id, DBObject object) {
        if (objects == null) objects = new HashMap<BigInteger, DBObject>();
        DBObject res = objects.get(id);
        objects.put(id, object);
        return res;
    }
    public static void removeFromCache(String id) {
        removeFromCache(new BigInteger(id));
    }
    public static void removeFromCache(BigInteger id) {
        if (objects == null) return;
        if (objects.containsKey(id)) objects.remove(id);
    }
}
