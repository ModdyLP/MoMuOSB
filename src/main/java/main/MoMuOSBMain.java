package main;

import de.moddylp.simplecommentconfig.Config;
import de.moddylp.simplecommentconfig.ConfigManager;
import discord.bot.Bot;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.ConfigLoader;
import storage.LanguageLoader;
import storage.api.Storage;

import java.sql.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class MoMuOSBMain {

    public static Date starttime = new Date(System.currentTimeMillis());
    public static final Logger logger = LogManager.getLogger();
    public static Config config;
    public static ConfigManager configManager = new ConfigManager();

    /**
     * main Method
     * @param args Start Argumente
     */
    public static void main(String[] args) {
        try {
            logger.info("=================================Bot initiating...======================================");
            logger.info("Bot was created by ModdyLP - Niklas H. https://moddylp.de.");
            config = configManager.getConfig("config/config.yml");
            config.setHeader(new String[] {"MoMuOSB Config", "Only edit if you know what you do."});
            logger.info("Creating Config: "+config.getAbsolutePath());
            ConfigLoader.loadConfigOptions();
            logger.info("Loading Language....");
            LanguageLoader.getInstance().createTranslations();
            logger.info("Language loading complete! "+LanguageLoader.lang.getAbsolutePath());
            Runtime.getRuntime().addShutdownHook(new Thread(MoMuOSBMain::shutdown));
            Bot.getBotInst().startBot();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Shutdown Method
     */
    public static void shutdown() {
        if (Bot.getBotInst().getBot() != null) {
            Bot.getBotInst().getBot().shutdownNow();
        }
        MoMuOSBMain.logger.info("====================================Bot shutting down...==============================");
        MoMuOSBMain.logger.info("ByeBye... Created by ModdyLP @2018");
    }
}
