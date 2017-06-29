package storage;

import main.Fast;
import util.Console;

/**
 * Created by N.Hartmann on 29.06.2017.
 * Copyright 2017
 */
public class LanguageLoader implements Fast{

    public String LANG;
    private String DEF_LANG = "lang_en.yml";
    public String ERROR = ":warning: ";

    private static LanguageLoader instance;
    /**
     * Get Instance
     * @return Class Instance
     */
    public static LanguageLoader getInstance() {
        if (instance == null) {
            instance = new LanguageLoader();
        }
        return instance;
    }

    /**
     * Create Language Files
     */
    public void createTranslations() {
        if (DRIVER.getProperty(DRIVER.CONFIG, "language", "en").equals("en")) {
            LANG = DEF_LANG;
        } else {
            LANG = "lang_"+ DRIVER.getProperty(DRIVER.CONFIG, "language", "en").equals("en")+".yml";
            DRIVER.createNewFile(LANG);
        }
        DRIVER.createNewFile(DEF_LANG);
    }

    /**
     * Get an Translation String
     * @param option option
     * @return String
     */
    public String getTranslation(String option) {
        return DRIVER.getProperty(LANG, option, DRIVER.getLangProperty(DEF_LANG, option).toString()).toString();
    }

    /**
     * Save Default Config to File
     */
    public void setDefaultLanguage() {
        Console.debug("DefaultLanguage Loading for Fallback");

        //Errors
        DRIVER.setProperty(DEF_LANG, "common_error", "There was an error!");
        DRIVER.setProperty(DEF_LANG, "commonmessage_error", "There was an error! Error: {1}");
        DRIVER.setProperty(DEF_LANG, "annotation_error", "Invalid Annotation in Module {1} Ex: {2}");
        DRIVER.setProperty(DEF_LANG, "token_error", "Please provide a token inside of the config.yml");
        DRIVER.setProperty(DEF_LANG, "execution_error", "Error occured on Command Exceution: {1}");

        DRIVER.setProperty(DEF_LANG, "notsend_error", "Message could not be sent! Error: {1}");
        DRIVER.setProperty(DEF_LANG, "notdeleted_error", "Message could not be deleted! Error: {1}");
        DRIVER.setProperty(DEF_LANG, "notsendpermission_error", "Message could not be send! The Bot has not enought Permissions for [{1}] - #{2} Error: {1}");
        DRIVER.setProperty(DEF_LANG, "private_error", "You can not use commands in direct messages");
        DRIVER.setProperty(DEF_LANG, "nopermissions_error", "You have no Permission to use this command.");
        DRIVER.setProperty(DEF_LANG, "nomanagepermission_error", "The Bot has no Permission to Manage Permissions");
        DRIVER.setProperty(DEF_LANG, "tofewarguments_error", "You have provieded to few arguments. {1} of {2}");
        DRIVER.setProperty(DEF_LANG, "tomanyarguments_error", "You have provieded to many arguments. {1} of {2}");
        DRIVER.setProperty(DEF_LANG, "botowner_error", "This command can only be used from Bot Owner.");
        DRIVER.setProperty(DEF_LANG, "deletion_error", "Deletion of Messages failed ({1} of {2}) Error: {3}");

        //Infos
        DRIVER.setProperty(DEF_LANG, "login_info", "Bot is logging in. Please wait until its ready...");

        //Stats Command
        DRIVER.setProperty(DEF_LANG, "stats_title", "General Stats");
        DRIVER.setProperty(DEF_LANG, "stats_servercount", "Servercount");
        DRIVER.setProperty(DEF_LANG, "stats_shards", "Shards");
        DRIVER.setProperty(DEF_LANG, "stats_owner", "Bot Owner");
        DRIVER.setProperty(DEF_LANG, "stats_user", "Users");
        DRIVER.setProperty(DEF_LANG, "stats_commands", "Commands");
        DRIVER.setProperty(DEF_LANG, "stats_uptime", "Uptime");

        //Help Command
        DRIVER.setProperty(DEF_LANG, "help_title", "All Commands");
        DRIVER.setProperty(DEF_LANG, "help_command", "Commands");
        DRIVER.setProperty(DEF_LANG, "help_alias", "Alias");
        DRIVER.setProperty(DEF_LANG, "help_arguments", "Arguments");
        DRIVER.setProperty(DEF_LANG, "help_description", "Description");

        //Deletion
        DRIVER.setProperty(DEF_LANG, "del_topic", "Deletion {1} of {2}");


    }

}
