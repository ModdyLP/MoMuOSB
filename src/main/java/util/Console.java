package util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;


/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Console implements Fast{

    private static String prefix = "[MoMuOSB]";
    public static String recievedprefix = "[R]";
    public static String sendprefix = "[S]";
    private final static Logger logger = LogManager.getLogger();

    /**
     * Debug Message
     * @param message message
     */
    public static void debug(String message) {
        if (DRIVER.getPropertyOnly(DRIVER.CONFIG,"debug").equals(true)) {
            String debugprefix = "[DEBUG]";
            String[] parts = message.split("\n");
            for (String part: parts) {
                //System.out.println(Utils.format(getTimeNow())+prefix + debugprefix + part);
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
            //System.out.println(Utils.format(getTimeNow())+prefix + errorprefix + part);
            logger.error(prefix + errorprefix + part);
        }
    }
    /**
     * Error message
     * @param message message
     */
    public static void error(Exception message) {
        String errorprefix = "[ERROR]";
        try {
            message.printStackTrace(new PrintWriter(new File("error.log")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        message.printStackTrace();
        logger.error(prefix+ errorprefix + System.lineSeparator() + message.getMessage());
    }

    /**
     * Info Message
     * @param message message
     */
    public static void println(String message) {
        String infoprefix = "[INFO]";
        String[] parts = message.split("\n");
        for (String part: parts) {
            //System.out.println(Utils.format(getTimeNow())+prefix + infoprefix + part);
            logger.info(prefix + infoprefix + part);
        }
    }

    private static Date getTimeNow() {
        return new Date(System.currentTimeMillis());
    }
}
