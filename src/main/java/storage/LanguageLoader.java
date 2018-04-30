package storage;

import de.moddylp.simplecommentconfig.Config;
import main.MoMuOSBMain;
import storage.api.Storage;

/**
 * Created by N.Hartmann on 29.06.2017.
 * Copyright 2017
 */
public class LanguageLoader implements Storage{

    public String SUCCESS = ":heavy_check_mark: ";
    public String ERROR = ":warning: ";

    private static LanguageLoader instance;
    public static Config lang;

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
        if (CONFIG.get("language", "en").equals("en")) {
            lang = MoMuOSBMain.configManager.getConfig("lang/lang_en.yml");
        } else {
            lang = MoMuOSBMain.configManager.getConfig("lang/lang_"+ CONFIG.get("language")+".yml");
        }
        setDefaultLanguage();
        lang.saveToFile();
    }

    /**
     * Get an Translation String
     * @param option option
     * @return String
     */
    public String getTranslation(String option) {
        return LANG.get(option).toString();
    }

    /**
     * Save Default Config to File
     */
    public void setDefaultLanguage() {
        MoMuOSBMain.logger.debug("DefaultLanguage Loading for Fallback");
        //Errors
        lang.get( "error.common_error", "There was an error!");
        lang.get( "error.commonmessage_error", "There was an error! Error: %1s");
        lang.get( "error.annotation_error", "Invalid Annotation in Module %1s Ex: %2s");
        lang.get( "error.token_error", "Please provide a token inside of the config.json");
        lang.get( "error.execution_error", "Error occurred on Command Execution: %1s");

        lang.get( "error.notsend_error", "Message could not be sent! Error: %1s");
        lang.get( "error.notdeleted_error", "Message could not be deleted! Error: %1s");
        lang.get( "error.notsendpermission_error", "Message could not be send! The Bot has not enough Permissions for [%1s] - #%2s Error: %3s");
        lang.get( "error.private_error", "You can not use commands in direct messages");
        lang.get( "error.nopermissions_error", "You have no Permission to use this command.");
        lang.get( "error.nomanagepermission_error", "The Bot has no Permission to Manage Messages.");
        lang.get( "error.tofewarguments_error", "You have provided to few arguments. %1s of %2s");
        lang.get( "error.tomanyarguments_error", "You have provided to many arguments. %1s of %2s");
        lang.get( "error.botowner_error", "This command can only be used from Bot Owner.");
        lang.get( "error.deletion_error", "Deletion of Messages failed (%1s of %2s) Error: %3s");
        lang.get( "info.deleteprivinfo", "Deletion was successfull.");
        lang.get( "info.role_permissions", "Bot requires a higher position in role Hierarchy to add the Gender roles");

        lang.get( "info.private_msg_not_owner", "You can not send Messages directly to other Users. <br> Please write a message without a mention to contact the Bot Owner.");

        //Infos
        lang.get( "info.login_info", "Bot sign into the server. Please wait until the Bot is ready...");
        lang.get( "info.command_success", "The command was successful!");
        lang.get( "info.command_success_wait", "The command was successful. Please wait... Result is generating...");
        lang.get( "info.shutdowninfo", "The Bot will shutting down in 10 seconds! Bye bye.");

        lang.saveToFile();
    }

}
