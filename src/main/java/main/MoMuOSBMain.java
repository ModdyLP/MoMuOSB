package main;

import discord.DiscordInit;
import storage.ConfigLoader;
import util.Console;
import util.Fast;

import java.sql.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class MoMuOSBMain implements Fast {

    public static Date starttime = new Date(System.currentTimeMillis());

    /**
     * main Method
     * @param args Start Argumente
     */
    public static void main(String[] args) {
        try {
            Console.println("=================================Bot starting...======================================");
            Console.println("Bot was created by ModdyLP - Niklas H. https://moddylp.de.");
            ConfigLoader.loadConfigOptions();
            Console.println("Loading Language....");
            LANG.createTranslations();
            LANG.setDefaultLanguage();
            Console.println("Language loading complete!");
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

    /**
     * Shutdown Method
     */
    public static void shutdown() {
        Console.println("====================================Bot shutting down...==============================");
        if (INIT.BOT != null && INIT.BOT.isLoggedIn()) {
            INIT.BOT.logout();
        }
        Console.println("ByeBye... Created by ModdyLP @2017");
    }
}
