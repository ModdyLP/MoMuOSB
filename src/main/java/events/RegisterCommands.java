package events;

import modules.*;
import modules.music.MainMusic;
import storage.LanguageMethod;
import util.Console;
import util.Fast;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class RegisterCommands implements Fast{
    /**
     * Commands Registration
     */
    static void registerAll() {
        Console.println("Command scanning...");
        //register commands
        COMMAND.registerCommand(InfoCommands.class, new InfoCommands());
        COMMAND.registerCommand(ChangeCommands.class, new ChangeCommands());
        COMMAND.registerCommand(Moderation.class, new Moderation());
        COMMAND.registerCommand(SearchCommand.class, new SearchCommand());
        COMMAND.registerCommand(MainMusic.class, new MainMusic());
        COMMAND.registerCommand(Permission.class, new Permission());
        COMMAND.registerCommand(Gamestats.class, new Gamestats());
        COMMAND.registerCommand(RoleManagement.class, new RoleManagement());
        COMMAND.registerCommand(ServerManager.class, new ServerManager());

        try {
            for (LanguageMethod language : COMMAND.languages.keySet()) {
                Console.debug("Moduleclass:  "+COMMAND.langinstances.get(language).getClass().getName());
                COMMAND.langinstances.get(language).setdefaultLanguage();
            }
        }catch (Exception ex) {
            Console.error(ex);
            System.exit(0);
        }
        DRIVER.saveJson();


        Console.println("All Commands("+COMMAND.getAllCommands().size()+") added to Bot");
    }
}
