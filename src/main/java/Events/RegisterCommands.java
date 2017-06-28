package Events;

import Modules.ChangeCommands;
import Modules.HelpCommand;
import Modules.StatsCommand;
import Util.Console;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
class RegisterCommands {
    static void registerAll() {
        Console.println("Command scanning...");
        //register commands
        EventListener.getInstance().registerCommand(HelpCommand.class, new HelpCommand());
        EventListener.getInstance().registerCommand(StatsCommand.class, new StatsCommand());
        EventListener.getInstance().registerCommand(ChangeCommands.class, new ChangeCommands());

        Console.println("All Commands("+EventListener.getInstance().getAllCommands().size()+") added to Bot");
    }
}
