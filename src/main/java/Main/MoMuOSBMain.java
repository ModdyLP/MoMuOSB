package Main;

import Discord.DiscordInit;
import Events.EventListener;
import Modules.HelpCommand;
import Modules.ChangeCommands;
import Modules.StatsCommand;
import Storage.ConfigDriver;
import Util.Console;

import java.util.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class MoMuOSBMain {

    private static boolean runstate = true;
    public static Date starttime = new Date(System.currentTimeMillis());

    public static void main(String[] args) {
        try {
            Console.println("Bot starting...");
            Console.println("Bot was created by ModdyLP - Niklas H. https://moddylp.de");
            ConfigDriver.getInstance().getProperty("invitepermission", "2146958591");
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    shutdown();
                }
            });
            DiscordInit.getInstance().init();

        } catch (Exception ex) {
            Console.error(ex.getMessage());
            ex.printStackTrace();
        }

    }

    public static void shutdown() {
        Console.println("Bot shutting down...");
        if (DiscordInit.getInstance().getDiscordClient().isLoggedIn()) {
            DiscordInit.getInstance().getDiscordClient().logout();
        }
    }


    public static void setRunstate(boolean state) {
        runstate = state;
    }
}
