package core;

import java.util.HashMap;
import java.util.Map;


public class RequestParams {
    private Map<String, Object> params;

    public final String PARAM_IP = "PARAM_IP";
    public final String PARAM_USER_AGENT = "PARAM_USER_AGENT";

    public RequestParams() {
        params = new HashMap<String, Object>();
    }
    public Object getParameter(final String key) {
        return params.get(key);
    }
    public RequestParams setParameter(final String key, Object value) {
        params.put(key, value);
        return this;
    }
}
