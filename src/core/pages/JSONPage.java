package core.pages;


import org.json.simple.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class JSONPage extends AbstractPage {
    protected JSONObject outObject;

    public JSONPage(HttpServletRequest request, HttpServletResponse response) {
        super(request, response);
        outObject = new JSONObject();
    }
    protected void putJSONError(String message) {
        outObject.put("code", 1);
        outObject.put("message", message);
    }
    protected void putJSONSuccess() {
        outObject.put("code", 0);
        if (outObject.containsKey("message")) {
            outObject.remove("message");
        }
    }
    protected void render() throws Exception {
        outObject.writeJSONString(out);
    }
}
