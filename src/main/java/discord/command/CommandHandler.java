package discord.command;

import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import main.MoMuOSBMain;
import storage.api.Storage;

public class CommandHandler implements Storage{

    public static CommandClient client;

    public static CommandClient registerCommands() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(MoMuOSBMain.config.get("bot.owner", "").toString());
        builder.setPrefix(MoMuOSBMain.config.get( "bot.prefix", "").toString()+"!");
        builder.addCommand(new InfoCommand());
        return null;
    }
}
