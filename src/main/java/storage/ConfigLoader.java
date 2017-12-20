package storage;

import storage.api.Storage;

/**
 * Created by N.Hartmann on 30.06.2017.
 * Copyright 2017
 */
public class ConfigLoader implements Storage {
    public static void loadConfigOptions() {
        DRIVER.createNewFile(DRIVER.CONFIG);

        //Generell Settings
        DRIVER.getProperty(DRIVER.CONFIG,"bot.Playing", "TestBetrieb");
        DRIVER.getProperty(DRIVER.CONFIG,"bot.UserName", "MoMuOSB");
        DRIVER.getProperty(DRIVER.CONFIG,"bot.Debug", false);
        DRIVER.getProperty(DRIVER.CONFIG, "bot.language", "en");
        DRIVER.getProperty(DRIVER.CONFIG,"bot.Shards", 3);
        DRIVER.getProperty(DRIVER.CONFIG,"bot.owner", "");

        //Custom Settings
        DRIVER.getProperty(DRIVER.CONFIG,"bot.DeleteAnswer.On", true);
        DRIVER.getProperty(DRIVER.CONFIG,"bot.DeleteAnswer.Time", 5);
        DRIVER.getProperty(DRIVER.CONFIG, "bot.prefix", "");
        DRIVER.getProperty(DRIVER.CONFIG, "bot.ownerbypass", true);

        //Music
        DRIVER.getProperty(DRIVER.CONFIG, "music.defaultvolume", 10);

        //Authentication
        DRIVER.getProperty(DRIVER.CONFIG, "auth.googleauthtoken", "");
        DRIVER.getProperty(DRIVER.CONFIG, "auth.googlecustomsearchid","002710779101845872719:o_wp4w-dqqi");
        DRIVER.getProperty(DRIVER.CONFIG,"auth.token", "");

        //Modules
        DRIVER.getProperty(DRIVER.CONFIG, "genderroles", false);

        DRIVER.saveJson();
    }
}
