package events;

import main.Fast;
import modules.ChangeCommands;
import modules.InfoCommands;
import modules.Moderation;
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
        EVENT.registerCommand(InfoCommands.class, new InfoCommands());
        EVENT.registerCommand(ChangeCommands.class, new ChangeCommands());
        EVENT.registerCommand(Moderation.class, new Moderation());

        Console.println("All Commands("+EVENT.getAllCommands().size()+") added to Bot");
    }
}
