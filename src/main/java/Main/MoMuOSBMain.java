package Main;

import Discord.DiscordInit;
import Events.EventListener;
import Modules.HelpCommand;
import Modules.ChangeCommands;
import Modules.StatsCommand;
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
            Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    shutdown();
                }
            });
            DiscordInit.getInstance().init();

            //register commands
            EventListener.getInstance().registerCommand(HelpCommand.class, new HelpCommand());
            EventListener.getInstance().registerCommand(StatsCommand.class, new StatsCommand());
            EventListener.getInstance().registerCommand(ChangeCommands.class, new ChangeCommands());

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
