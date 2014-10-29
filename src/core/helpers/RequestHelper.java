package core.helpers;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 19.10.13
 * Time: 2:51
 * To change this template use File | Settings | File Templates.
 */
public class RequestHelper {
    public static core.database.DBObject createRequest(String author, List subjects, String phone, String text) {
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        List tmp;
        if (author != null) {
            params.put(new BigInteger("31"), tmp = new ArrayList());
            tmp.add(author);
        }
        params.put(new BigInteger("30"), tmp = new ArrayList());
        tmp.add("now()");
        if (subjects != null) {
            params.put(new BigInteger("13"), subjects);
        }
        if (phone != null) {
            params.put(new BigInteger("32"), tmp = new ArrayList());
            tmp.add(phone);
        }
        if (text != null) {
            params.put(new BigInteger("28"), tmp = new ArrayList());
            tmp.add(text);
        }
        return DBHelper.createObject("8", "request from " + (author != null ? author : "unknown"), "request from site", params);
    }
    public static core.database.DBObject createRegisterRequest(String author, String phone, String text) {
        Map<BigInteger, List> params = new HashMap<BigInteger, List>();
        List tmp;
        if (author != null) {
            params.put(new BigInteger("31"), tmp = new ArrayList());
            tmp.add(author);
        }
        params.put(new BigInteger("30"), tmp = new ArrayList());
        tmp.add("now()");
        if (phone != null) {
            params.put(new BigInteger("32"), tmp = new ArrayList());
            tmp.add(phone);
        }
        if (text != null) {
            params.put(new BigInteger("28"), tmp = new ArrayList());
            tmp.add(text);
        }
        return DBHelper.createObject("9", "register request from " + (author != null ? author : "unknown"), "register request from site", params);
    }
}
