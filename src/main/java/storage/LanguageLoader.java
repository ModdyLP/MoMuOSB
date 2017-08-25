package storage;

import events.Command;
import util.Fast;
import util.Console;

/**
 * Created by N.Hartmann on 29.06.2017.
 * Copyright 2017
 */
public class LanguageLoader implements Fast{

    public String SUCCESS = ":heavy_check_mark: ";
    public String LANG;
    private String DEF_LANG = "lang/lang_en.json";
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
        if (DRIVER.getPropertyOnly(DRIVER.CONFIG, "language").equals("en")) {
            LANG = DEF_LANG;
        } else {
            LANG = "lang/lang_"+ DRIVER.getPropertyOnly(DRIVER.CONFIG, "language")+".json";
            DRIVER.createNewFile(LANG);
        }
        DRIVER.createNewFile(DEF_LANG);
    }
    public String getMethodDescription(Command command) {
        return DRIVER.getProperty(LANG, COMMAND.getMethodNameByCommand(command)+"_"+command.command(), command.description()).toString();
    }

    /**
     * Get an Translation String
     * @param option option
     * @return String
     */
    public String getTranslation(String option) {
        return DRIVER.getProperty(LANG, option, DRIVER.getPropertyOnly(DEF_LANG, option).toString()).toString();
    }

    /**
     * Save Default Config to File
     */
    public void setDefaultLanguage() {
        Console.debug("DefaultLanguage Loading for Fallback");

        //Errors
        DRIVER.setProperty(DEF_LANG, "common_error", "There was an error!");
        DRIVER.setProperty(DEF_LANG, "commonmessage_error", "There was an error! Error: %1s");
        DRIVER.setProperty(DEF_LANG, "annotation_error", "Invalid Annotation in Module %1s Ex: %2s");
        DRIVER.setProperty(DEF_LANG, "token_error", "Please provide a token inside of the config.json");
        DRIVER.setProperty(DEF_LANG, "execution_error", "Error occurred on Command Execution: %1s");

        DRIVER.setProperty(DEF_LANG, "notsend_error", "Message could not be sent! Error: %1s");
        DRIVER.setProperty(DEF_LANG, "notdeleted_error", "Message could not be deleted! Error: %1s");
        DRIVER.setProperty(DEF_LANG, "notsendpermission_error", "Message could not be send! The Bot has not enough Permissions for [%1s] - #%2s Error: %3s");
        DRIVER.setProperty(DEF_LANG, "private_error", "You can not use commands in direct messages");
        DRIVER.setProperty(DEF_LANG, "nopermissions_error", "You have no Permission to use this command.");
        DRIVER.setProperty(DEF_LANG, "nomanagepermission_error", "The Bot has no Permission to Manage Messages.");
        DRIVER.setProperty(DEF_LANG, "tofewarguments_error", "You have provided to few arguments. %1s of %2s");
        DRIVER.setProperty(DEF_LANG, "tomanyarguments_error", "You have provided to many arguments. %1s of %2s");
        DRIVER.setProperty(DEF_LANG, "botowner_error", "This command can only be used from Bot Owner.");
        DRIVER.setProperty(DEF_LANG, "deletion_error", "Deletion of Messages failed (%1s of %2s) Error: %3s");
        DRIVER.setProperty(DEF_LANG, "deleteprivinfo", "Deletion was successfull.");
        DRIVER.setProperty(DEF_LANG, "role_permissions", "Bot requires a higher position in role Hierarchy to add the Gender roles");

        DRIVER.setProperty(DEF_LANG, "private_msg_not_owner", "You can not send Messages directly to other Users. \nPlease write a message without a mention to contact the Bot Owner.");

        //Infos
        DRIVER.setProperty(DEF_LANG, "login_info", "Bot sign into the server. Please wait until the Bot is ready...");
        DRIVER.setProperty(DEF_LANG, "command_success", "The command was successful!");
        DRIVER.setProperty(DEF_LANG, "command_success_wait", "The command was successful. Please wait... Result is generating...");
        DRIVER.setProperty(DEF_LANG, "shutdowninfo", "The Bot will shutting down in 10 seconds! Bye bye.");

        DRIVER.saveJson();
    }

}
