package events;

import modules.*;
import util.Fast;
import modules.music.MainMusic;
import util.Console;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
class RegisterCommands implements Fast{
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


        Console.println("All Commands("+COMMAND.getAllCommands().size()+") added to Bot");
    }
}
