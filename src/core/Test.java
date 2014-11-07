package core;

import core.cache.ObjectCache;
import core.cache.ObjectTypeCache;
import core.database.Attributes;
import core.database.DBObject;
import core.entity.CampTypes;
import core.helpers.DBHelper;
import core.helpers.TempHelper;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class Test {
    public static void main(String[] args) {
        String[] exts = {"jpg", "jpeg", "gif", "png"};
        List<String> extensions = Arrays.asList(exts);
        String r = "screen2.png";
        String ext = r.substring(r.lastIndexOf("."));
    }
}
