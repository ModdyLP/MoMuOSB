package main;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import storage.ConfigLoader;
import storage.api.Storage;

import java.sql.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class MoMuOSBMain {

    public static Date starttime = new Date(System.currentTimeMillis());
    public static final Logger logger = LogManager.getLogger();

    /**
     * main Method
     * @param args Start Argumente
     */
    public static void main(String[] args) {
        try {
            logger.info("=================================Bot starting...======================================");
            logger.info("Bot was created by ModdyLP - Niklas H. https://moddylp.de.");
            ConfigLoader.loadConfigOptions();
            logger.info("Loading Language....");

            logger.info("Language loading complete!");


            Runtime.getRuntime().addShutdownHook(new Thread() {
                        public void run() {
                            shutdown();
                        }
                    });

        } catch (Exception ex) {
            logger.error(ex.getMessage());
            logger.error(ex);
        }
    }

    /**
     * Shutdown Method
     */
    public static void shutdown() {
        MoMuOSBMain.logger.info("====================================Bot shutting down...==============================");
        MoMuOSBMain.logger.info("ByeBye... Created by ModdyLP @2017");
    }
}
