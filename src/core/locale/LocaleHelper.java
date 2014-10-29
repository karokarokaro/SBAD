package core.locale;

import core.Logger;
import core.helpers.SQLController;

import java.math.BigInteger;
import java.sql.ResultSet;


public class LocaleHelper {
    private static final String SQL_GET_LOCALE_TEXT = "select text from locale where locale_id={locId} and lang_id={lang}";

    public static final BigInteger LANG_RUS = new BigInteger("1");

    public static String getLocaleText(BigInteger localeId, BigInteger langID) {
        String res = "";
        try{
            ResultSet resultSet = SQLController.executeSelect(SQL_GET_LOCALE_TEXT
                    .replace("{locId}", localeId.toString())
                    .replace("{lang}", langID.toString()));
            if (resultSet.next()) res = resultSet.getString("text");
            if (res == null) res = "";
        } catch (Exception e) {
            Logger.error(e);
        }
        return res;
    }
    public static String getLocaleText(String localeId) {
        return getLocaleText(new BigInteger(localeId), LANG_RUS);
    }
    public static String getLocaleText(BigInteger localeId) {
        return getLocaleText(localeId, LANG_RUS);
    }
}
