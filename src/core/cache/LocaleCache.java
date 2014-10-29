package core.cache;


import core.database.DBObject;
import core.helpers.DBHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.math.BigInteger;

public class LocaleCache {
    private static Map<BigInteger, Map<BigInteger, String>> locales;
    private static Map<BigInteger, List<BigInteger>> localesToLoad;

    public static void reset() {
        locales = null;
    }

    private static void flush() {
        Map<BigInteger, Map<BigInteger, String>> locs = DBHelper.getLocales(localesToLoad);
        localesToLoad = null;
        for (BigInteger lang: locs.keySet()) {
            for (BigInteger loc: locs.get(lang).keySet()) {
                addToCache(loc, lang, locs.get(lang).get(loc));
            }
        }
    }
    public static void addLocaleToLoad(String locId, String langId) {
        addLocaleToLoad(new BigInteger(locId), new BigInteger(langId));
    }
    public static void addLocaleToLoad(BigInteger locId, BigInteger landId) {
        if (locales != null && locales.containsKey(landId) && locales.get(landId).containsKey(locId)) return;
        if (localesToLoad == null) localesToLoad = new HashMap<BigInteger, List<BigInteger>>();
        if (!localesToLoad.containsKey(landId)) localesToLoad.put(landId, new ArrayList<BigInteger>());
        if (!localesToLoad.get(landId).contains(locId)) localesToLoad.get(landId).add(locId);
    }
    public static String getLocaleText(String locId, String langId) {
        return getLocaleText(new BigInteger(locId), new BigInteger(langId));
    }
    public static String getLocaleText(BigInteger locId, BigInteger landId) {
        if (localesToLoad != null) flush();
        if (locales == null) {
            locales = new HashMap<BigInteger, Map<BigInteger, String>>();
        } else {
            if (locales.containsKey(landId) && locales.get(landId) != null && locales.get(landId).containsKey(locId)) {
                return locales.get(landId).get(locId);
            }
        }
        addLocaleToLoad(locId, landId);
        flush();
        if (locales.containsKey(landId) && locales.get(landId) != null && locales.get(landId).containsKey(locId)) {
            return locales.get(landId).get(locId);
        }
        return null;
    }
    public static String addToCache(String locId, String langId, String text) {
        return addToCache(new BigInteger(locId), new BigInteger(langId), text);
    }
    public static String addToCache(BigInteger locId, BigInteger langId, String text) {
        if (locales == null) locales = new HashMap<BigInteger, Map<BigInteger, String>>();
        if (!locales.containsKey(langId)) locales.put(langId, new HashMap<BigInteger, String>());
        String res = locales.get(langId).get(locId);
        locales.get(langId).put(locId, text);
        return res;
    }
    public static void removeFromCache(String locId, String langId) {
        removeFromCache(new BigInteger(locId), new BigInteger(langId));
    }

    public static void removeFromCache(BigInteger locId, BigInteger langId) {
        if (locales == null) return;
        if (!locales.containsKey(langId)) return;
        locales.get(langId).remove(locId);
    }

}
