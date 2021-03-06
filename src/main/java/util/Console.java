package util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Date;


/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Console implements Fast{

    private static String prefix = "[MoMuOSB]";
    public static String recievedprefix = "[R]";
    public static String sendprefix = "[S]";
    final static Logger logger = LoggerFactory.getLogger(Console.class);

    /**
     * Debug Message
     * @param message message
     */
    public static void debug(String message) {
        if (DRIVER.getPropertyOnly(DRIVER.CONFIG,"debug").equals(true)) {
            String debugprefix = "[DEBUG]";
            String[] parts = message.split("\n");
            for (String part: parts) {
                System.out.println(Utils.format(getTimeNow())+prefix + debugprefix + part);
                logger.info(prefix + debugprefix + part);
            }
        }

    }

    /**
     * Error message
     * @param message message
     */
    public static void error(String message) {
        String errorprefix = "[ERROR]";
        String[] parts = message.split("\n");
        for (String part: parts) {
            System.out.println(Utils.format(getTimeNow())+prefix + errorprefix + part);
            logger.error(prefix + errorprefix + part);
        }
    }
    /**
     * Error message
     * @param message message
     */
    public static void error(Exception message) {
        String errorprefix = "[ERROR]";
        for(StackTraceElement stackTraceElement : message.getStackTrace()) {
            System.out.println(Utils.format(getTimeNow())+prefix + errorprefix + System.lineSeparator() + stackTraceElement.toString());
            logger.error(prefix+ errorprefix + System.lineSeparator() + stackTraceElement.toString());
        }
    }

    /**
     * Info Message
     * @param message message
     */
    public static void println(String message) {
        String infoprefix = "[INFO]";
        String[] parts = message.split("\n");
        for (String part: parts) {
            System.out.println(Utils.format(getTimeNow())+prefix + infoprefix + part);
            logger.info(prefix + infoprefix + part);
        }
    }

    private static Date getTimeNow() {
        return new Date(System.currentTimeMillis());
    }
}
