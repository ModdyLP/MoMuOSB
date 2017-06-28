package Util;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Console {

    private static String prefix = "[MoMuOSB]";
    private static String debugprefix = "[DEBUG]";
    private static String errorprefix = "[ERROR]";
    private static String infoprefix = "[INFO]";

    public static void debug(String message) {
        System.out.println(prefix + debugprefix + message);
    }
    public static void error(String message) {
        System.out.println(prefix + errorprefix + message);
    }
    public static void println(String message) {
        System.out.println(prefix + infoprefix + message);
    }
}
