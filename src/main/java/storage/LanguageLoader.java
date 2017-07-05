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
        DRIVER.loadJson();

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

        //Infos
        DRIVER.setProperty(DEF_LANG, "login_info", "Bot sign into the server. Please wait until the Bot is ready...");
        DRIVER.setProperty(DEF_LANG, "command_success", "The command was successful!");
        DRIVER.setProperty(DEF_LANG, "command_success_wait", "The command was successful. Please wait... Result is generating...");
        DRIVER.setProperty(DEF_LANG, "shutdowninfo", "The Bot will shutting down in 10 seconds! Bye bye.");

        //Stats Command
        DRIVER.setProperty(DEF_LANG, "stats_title", "General Stats");
        DRIVER.setProperty(DEF_LANG, "stats_servercount", "Server Count");
        DRIVER.setProperty(DEF_LANG, "stats_shards", "Shards");
        DRIVER.setProperty(DEF_LANG, "stats_owner", "Bot Owner");
        DRIVER.setProperty(DEF_LANG, "stats_user", "Users");
        DRIVER.setProperty(DEF_LANG, "stats_commands", "Commands");
        DRIVER.setProperty(DEF_LANG, "stats_uptime", "Uptime");

        //Help Command
        DRIVER.setProperty(DEF_LANG, "help_title", "All Commands");
        DRIVER.setProperty(DEF_LANG, "help_command", "Command");
        DRIVER.setProperty(DEF_LANG, "help_alias", "Alias");
        DRIVER.setProperty(DEF_LANG, "help_arguments", "Arguments");
        DRIVER.setProperty(DEF_LANG, "help_description", "Description");
        DRIVER.setProperty(DEF_LANG, "help_noneinfo", "If you want to reset a Value, then type for each argument NA.");
        DRIVER.setProperty(DEF_LANG, "help_prefixinfo", "\nThe Prefixes are \n" +
                "Admin Prefix:   !   \n" +
                "Info Prefix:    .   \n" +
                "Game Prefix:    ~   \n" +
                "music Prefix:   $   \n");
        DRIVER.setProperty(DEF_LANG, "help_permission", "Permission");

        //Deletion
        DRIVER.setProperty(DEF_LANG, "del_topic", "Deletion %1s of %2s");

        //Search
        DRIVER.setProperty(DEF_LANG, "engine_unknown", "The Search Engine is unknown!");
        DRIVER.setProperty(DEF_LANG, "results_end", "You reached the end of results.");
        DRIVER.setProperty(DEF_LANG, "results_cleared", "The results were cleared.");
        DRIVER.setProperty(DEF_LANG, "searchtoken_google", "Please provide a google search api token.");
        DRIVER.setProperty(DEF_LANG, "result_out", "The Result is out of Range.");
        DRIVER.setProperty(DEF_LANG, "no_search", "There is no search running.");
        DRIVER.setProperty(DEF_LANG, "search_result", "Your search Result");
        DRIVER.setProperty(DEF_LANG, "search_info", "Search Query");
        DRIVER.setProperty(DEF_LANG, "search_count", "Result %1s of %2s");

        //Music
        DRIVER.setProperty(DEF_LANG, "music_notinchannel", "The Bot is not in a voice Channel.");
        DRIVER.setProperty(DEF_LANG, "music_notinchannel_user", "You are not in a Voice Channel.");
        DRIVER.setProperty(DEF_LANG, "music_volumechange", "Volume changed from %1s to %2s.");
        DRIVER.setProperty(DEF_LANG, "music_volumechangeerror", "Can't change volume.");
        DRIVER.setProperty(DEF_LANG, "music_add","Adding to queue: %1s.");
        DRIVER.setProperty(DEF_LANG, "music_add_queue", "Adding to queue %1s (first track of playlist %2s).");
        DRIVER.setProperty(DEF_LANG, "music_notfound", "Nothing found by %1s.");
        DRIVER.setProperty(DEF_LANG, "music_notloaded", "Could not play the choosen song.");
        DRIVER.setProperty(DEF_LANG, "music_skip", "Skipped to next track.");
        DRIVER.setProperty(DEF_LANG, "disabledserver", "This server is disabled for using the Music Module");

        //Changes
        DRIVER.setProperty(DEF_LANG, "changeprop_error", "This option can't found in the config file!");
        DRIVER.setProperty(DEF_LANG, "notchanged_error", "The change Command does not provide resetting.");
        DRIVER.setProperty(DEF_LANG, "parse_error", "The Value cant parsed into a valid format.");
        DRIVER.setProperty(DEF_LANG, "props", "Properties");

        //Permission
        DRIVER.setProperty(DEF_LANG, "perm_add_success", "Permission added successful.");
        DRIVER.setProperty(DEF_LANG, "perm_add_failed", "Failed to add Permission to group.");
        DRIVER.setProperty(DEF_LANG, "perm_rem_success", "Permission removed successful.");
        DRIVER.setProperty(DEF_LANG, "perm_rem_failed", "Failed to remove Permission to group.");
        DRIVER.setProperty(DEF_LANG, "permlist_title", "Permission List for Group %1s");
        DRIVER.setProperty(DEF_LANG, "norolefound", "The Role was not found.");

        DRIVER.saveJson();
    }

}
