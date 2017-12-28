package main;

import discord.BotUtils;
import discord.DiscordInit;
import discord.Stats;
import modules.RoleManagement;
import modules.music.MainMusic;
import storage.ConfigLoader;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;

import java.sql.Date;
import java.util.Objects;

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

            Stats.loadStats();

            Runtime.getRuntime().addShutdownHook(new Thread() {
                        public void run() {
                            shutdown();
                        }
                    });
            DiscordInit.getInstance().init();

        } catch (Exception ex) {
            Console.error(ex.getMessage());
            Console.error(ex);
        }

    }

    /**
     * Shutdown Method
     */
    public static void shutdown() {
        Console.println("====================================Bot shutting down...==============================");
        Stats.saveStats();
        RoleManagement.saveGenders();
        for (IGuild guild: Objects.requireNonNull(INIT.BOT != null ? INIT.BOT.getGuilds() : null)) {
            if (MainMusic.playmessages.containsKey(guild)) {
                BotUtils.deleteMessageOne(MainMusic.playmessages.get(guild));
                MainMusic.playmessages.remove(guild);
            }
        }
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (INIT.BOT != null && INIT.BOT.isLoggedIn()) {
            INIT.BOT.logout();
        }


        Console.println("ByeBye... Created by ModdyLP @2017");
    }
}
