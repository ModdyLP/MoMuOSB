package Events;

import Modules.ChangeCommands;
import Modules.InfoCommands;
import Modules.Moderation;
import Util.Console;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
class RegisterCommands {
    static void registerAll() {
        Console.println("Command scanning...");
        //register commands
        EventListener.getInstance().registerCommand(InfoCommands.class, new InfoCommands());
        EventListener.getInstance().registerCommand(ChangeCommands.class, new ChangeCommands());
        EventListener.getInstance().registerCommand(Moderation.class, new Moderation());

        Console.println("All Commands("+EventListener.getInstance().getAllCommands().size()+") added to Bot");
    }
}
