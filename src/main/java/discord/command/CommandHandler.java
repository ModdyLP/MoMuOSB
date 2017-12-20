package discord.command;

import com.jagrosh.jdautilities.commandclient.CommandClient;
import com.jagrosh.jdautilities.commandclient.CommandClientBuilder;
import storage.api.Storage;

public class CommandHandler implements Storage{

    public static CommandClient client;

    public static CommandClient registerCommands() {
        CommandClientBuilder builder = new CommandClientBuilder();
        builder.setOwnerId(DRIVER.getProperty(DRIVER.CONFIG, "bot.owner", "").toString());
        builder.setPrefix(DRIVER.getProperty(DRIVER.CONFIG, "bot.prefix", "").toString()+"!");
        builder.addCommand(new InfoCommand());
        client
        return ;
    }
}
