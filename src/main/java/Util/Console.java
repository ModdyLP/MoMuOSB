package Util;

import Storage.ConfigDriver;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Console {

    private static String prefix = "[MoMuOSB]";
    public static String recievedprefix = "[R]";
    public static String sendprefix = "[S]";

    public static void debug(String message) {
        if (ConfigDriver.getInstance().getProperty("debug", "false").equals("true")) {
            String debugprefix = "[DEBUG]";
            System.out.println(prefix + debugprefix + message);
        }
    }
    public static void error(String message) {
        String errorprefix = "[ERROR]";
        System.out.println(prefix + errorprefix + message);
    }
    public static void println(String message) {
        String infoprefix = "[INFO]";
        System.out.println(prefix + infoprefix + message);
    }
}
