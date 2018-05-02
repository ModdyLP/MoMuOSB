package discord.command;

import com.jagrosh.jdautilities.command.CommandClient;
import com.jagrosh.jdautilities.command.CommandClientBuilder;
import main.MoMuOSBMain;
import storage.api.Storage;

public class CommandHandler implements Storage {

    public static CommandClient client;

    public static CommandClient registerCommands() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(MoMuOSBMain.config.get("bot.owner", "").toString());
        builder.setPrefix(MoMuOSBMain.config.get("bot.prefix", "").toString() + "!");
        builder.setEmojis("", "", "");
        builder.addCommand(new InfoCommand());
        client = builder.build();
        return client;
    }
}
