package core.helpers;

import core.cache.SearchCache;
import core.entity.search.SearchParams;
import core.entity.search.SearchResult;

public class SearchHelper {
    public static SearchResult search(SearchParams params) {
        String paramsHash = params.getHash();
        SearchResult res = SearchCache.getResult(paramsHash);
        if (res != null) return res;
        res = executeSearch(params);
        if (res != null) SearchCache.addToCache(paramsHash, res);
        return res;
    }
    public static SearchResult executeSearch(SearchParams params) {
        return null;
    }
}
