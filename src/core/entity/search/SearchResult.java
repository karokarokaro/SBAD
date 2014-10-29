package core.entity.search;


import core.database.DBObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
    private List<DBObject> objectList;
    private int count;

    public SearchResult() {
        objectList = new ArrayList<DBObject>();
    }

    public List<DBObject> getObjectList() {
        return objectList;
    }

    public SearchResult setObjectList(List<DBObject> objectList) {
        this.objectList = objectList;
        return this;
    }

    public int getCount() {
        return count;
    }

    public SearchResult setCount(int count) {
        this.count = count;
        return this;
    }
}
