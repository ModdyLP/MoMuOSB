package util;

import main.Fast;
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
            System.out.println(Utils.format(getTimeNow())+prefix + debugprefix + message);
            logger.info(prefix + debugprefix + message);
        }

    }

    /**
     * Error message
     * @param message message
     */
    public static void error(String message) {
        String errorprefix = "[ERROR]";
        System.out.println(Utils.format(getTimeNow())+prefix + errorprefix + message);
        logger.error(prefix + errorprefix + message);
    }

    /**
     * Info Message
     * @param message message
     */
    public static void println(String message) {
        String infoprefix = "[INFO]";
        System.out.println(Utils.format(getTimeNow())+prefix + infoprefix + message);
        logger.info(prefix + infoprefix + message);
    }

    private static Date getTimeNow() {
        return new Date(System.currentTimeMillis());
    }
}
