package util;

import main.Fast;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Console implements Fast{

    private static String prefix = "[MoMuOSB]";
    public static String recievedprefix = "[R]";
    public static String sendprefix = "[S]";

    /**
     * Debug Message
     * @param message message
     */
    public static void debug(String message) {
        if (DRIVER.getProperty(DRIVER.CONFIG,"debug", false).equals(true)) {
            String debugprefix = "[DEBUG]";
            System.out.println(prefix + debugprefix + message);
        }
    }

    /**
     * Error message
     * @param message message
     */
    public static void error(String message) {
        String errorprefix = "[ERROR]";
        System.out.println(prefix + errorprefix + message);
    }

    /**
     * Info Message
     * @param message message
     */
    public static void println(String message) {
        String infoprefix = "[INFO]";
        System.out.println(prefix + infoprefix + message);
    }
}
