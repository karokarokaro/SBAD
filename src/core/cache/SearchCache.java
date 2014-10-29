package core.cache;

import core.database.DBObject;
import core.entity.search.SearchResult;
import core.helpers.DBHelper;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCache {
    private static Map<String, SearchResult> results;
    public static void reset() {
        results = null;
    }
    public static SearchResult getResult(String paramsHash) {
        if (results == null) return null;
        return results.get(paramsHash);
    }
    public static SearchResult addToCache(String paramsHash, SearchResult searchResult) {
        if (results == null) results = new HashMap<String, SearchResult>();
        SearchResult res = results.get(paramsHash);
        results.put(paramsHash, searchResult);
        return res;
    }
}
