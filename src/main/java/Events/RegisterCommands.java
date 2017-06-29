package Events;

import Main.Fast;
import Modules.ChangeCommands;
import Modules.InfoCommands;
import Modules.Moderation;
import Util.Console;

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
