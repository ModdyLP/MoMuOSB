package storage;

import main.MoMuOSBMain;
import storage.api.Storage;

/**
 * Created by N.Hartmann on 30.06.2017.
 * Copyright 2017
 */
public class ConfigLoader implements Storage {
    public static void loadConfigOptions() {
        //Generell Settings
        CONFIG.get("bot.Playing", "TestBetrieb");
        CONFIG.get("bot.UserName", "MoMuOSB");
        CONFIG.get("bot.Debug", false);
        CONFIG.get("bot.language", "en");
        CONFIG.get("bot.Shards", 3);
        CONFIG.get("bot.owner", "");

        //Custom Settings
        CONFIG.get("bot.DeleteAnswer.On", true);
        CONFIG.get("bot.DeleteAnswer.Time", 5);
        CONFIG.get("bot.prefix", "");
        CONFIG.get("bot.ownerbypass", true);

        //Music
        CONFIG.get( "music.defaultvolume", 10);

        //Authentication
        CONFIG.get( "auth.googleauthtoken", "");
        CONFIG.get("auth.googlecustomsearchid","002710779101845872719:o_wp4w-dqqi");
        CONFIG.get("auth.token", "");

        //Modules
        CONFIG.get( "modules.genderroles", false);

        CONFIG.saveToFile();
    }
}
