package core.entity.search;


import java.util.ArrayList;
import java.util.List;

public class SearchParams {
    private List<SearchCriteria> criterias;
    private int limitStart;
    private int limitCount;

    public SearchParams() {
        criterias = new ArrayList<SearchCriteria>();
    }

    public void addCriteria(SearchCriteria searchCriteria) {
        criterias.add(searchCriteria);
    }

    public List<SearchCriteria> getCriterias() {
        return criterias;
    }

    public String getHash() {
        return "hashCode";
    }

    public int getLimitStart() {
        return limitStart;
    }

    public SearchParams setLimitStart(int limitStart) {
        this.limitStart = limitStart;
        return this;
    }

    public int getLimitCount() {
        return limitCount;
    }

    public SearchParams setLimitCount(int limitCount) {
        this.limitCount = limitCount;
        return this;
    }
}
