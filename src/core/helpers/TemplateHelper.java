package core.helpers;


import core.cache.ObjectCache;
import core.cache.ObjectTypeCache;
import core.database.Attributes;
import core.database.DBObject;
import freemarker.template.Template;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TemplateHelper {
    public static String apply(String templateString, Map params) throws Exception {
        Template template = new Template(null, new StringReader(templateString), null);
        StringWriter writer = new StringWriter();
        template.process(params, writer);
        return writer.toString();
    }
    public static String getTemplateString(BigInteger templateId) {
        DBObject templ = ObjectCache.getObject(templateId);
        return templ.getAttributeById(Attributes.TEXT).getTextValue();
    }
}
