package core.entity.search;

import core.database.DBAttribute;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 16.02.14
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class SearchCriteria {
    private List<DBAttribute> attributes;
    private SearchFunctor functor;
    private String value;

    public SearchCriteria() {
        attributes = new ArrayList<DBAttribute>();
    }

    public SearchFunctor getFunctor() {
        return functor;
    }

    public SearchCriteria setFunctor(SearchFunctor functor) {
        this.functor = functor;
        return this;
    }

    public String getValue() {
        return value;
    }

    public SearchCriteria setValue(String value) {
        this.value = value;
        return this;
    }

    public List<DBAttribute> getAttributes() {
        return attributes;
    }

    public void addAttributes(DBAttribute attr) {
        attributes.add(attr);
    }
}
