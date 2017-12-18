package storage;

import storage.api.Storage;

/**
 * Created by N.Hartmann on 30.06.2017.
 * Copyright 2017
 */
public class ConfigLoader implements Storage {
    public static void loadConfigOptions() {
        DRIVER.createNewFile(DRIVER.CONFIG);
        DRIVER.getProperty(DRIVER.CONFIG,"botanswerdeletseconds", 5);
        DRIVER.getProperty(DRIVER.CONFIG,"debug", false);
        DRIVER.getProperty(DRIVER.CONFIG,"defaultplaying", "TestBetrieb");
        DRIVER.getProperty(DRIVER.CONFIG,"defaultUsername", "MoMuOSB");
        DRIVER.getProperty(DRIVER.CONFIG, "defaultvolume", 10);
        DRIVER.getProperty(DRIVER.CONFIG,"deleteBotAnswers", true);
        DRIVER.getProperty(DRIVER.CONFIG, "googleauthtoken", "");
        DRIVER.getProperty(DRIVER.CONFIG, "googlecustomsearchid","002710779101845872719:o_wp4w-dqqi");
        DRIVER.getProperty(DRIVER.CONFIG, "language", "en");
        DRIVER.getProperty(DRIVER.CONFIG,"token", "");
        DRIVER.getProperty(DRIVER.CONFIG, "ownerbypass", true);
        DRIVER.getProperty(DRIVER.CONFIG, "botprefix", "");
        DRIVER.getProperty(DRIVER.CONFIG, "genderroles", false);
        DRIVER.saveJson();
    }
}
