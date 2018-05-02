package discord.command;

import com.jagrosh.jdautilities.command.Command;
import com.jagrosh.jdautilities.command.CommandEvent;

public class InfoCommand extends Command {

    public InfoCommand() {
        this.name = "info";
        this.aliases = new String[]{"i"};
        this.help = "Info About the Bot";
    }

    @Override
    protected void execute(CommandEvent commandEvent) {
        commandEvent.reactSuccess();
        commandEvent.replyInDm("Info Message");
        commandEvent.getMessage().delete();
    }
}
