package core;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: KARO
 * Date: 18.09.13
 * Time: 18:15
 * To change this template use File | Settings | File Templates.
 */
public class Logger {
    private static final String LOGGER_MODE_ERROR = "ERROR";
    private static final String LOGGER_MODE_DEBUG = "DEBUG";
    private static void log(String logText, String mode) {
        Date date = Calendar.getInstance().getTime();
        String fileName = new SimpleDateFormat("dd_MMM_yyyy HH").format(date) + "h";
        String logTime = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss").format(date);
        try{
            String separator = System.getProperty("line.separator");
            FileWriter fileWriter = new FileWriter(WebConfig.LOGGER_DIR + fileName + ".log", true);
            fileWriter.append(mode + " at " + logTime);
            fileWriter.append(separator);
            fileWriter.append(logText);
            fileWriter.append(separator + "----------------------------------------------------------" + separator);
            fileWriter.close();
        } catch (Exception e) {

        }
    }
    private static void log(Exception e, String mode) {
        StringWriter stringWriter = new StringWriter();
        e.printStackTrace(new PrintWriter(stringWriter));
        log(stringWriter.toString(), mode);
    }
    public static void error(String errorText) {
        log(errorText, LOGGER_MODE_ERROR);
    }
    public static void error(Exception e) {
        log(e, LOGGER_MODE_ERROR);
    }
    public static void debug(String debugText) {
        log(debugText, LOGGER_MODE_DEBUG);
    }
    public static void debug(Exception e) {
        log(e, LOGGER_MODE_DEBUG);
    }
}
