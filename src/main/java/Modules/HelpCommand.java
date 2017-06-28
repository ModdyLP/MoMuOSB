package Modules;

import Discord.BotUtils;
import Events.Command;
import Events.EventListener;
import Events.Module;
import Util.Footer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class HelpCommand extends Module {

    public static Module instance;

    @Command(
            command = "help",
            description = "Display the help",
            alias = "h",
            permission = Permissions.READ_MESSAGES
    )
    public boolean help(MessageReceivedEvent event, String[] args) {
        BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), genbuild().build());
        return true;
    }

    public EmbedBuilder genbuild() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(":information_source: All Commands :information_source:");
        builder.withColor(Color.CYAN);
        for (Command command : EventListener.getInstance().getAllCommands()) {
            builder.appendDesc("\nCommand:   "+BotUtils.BOT_PREFIX+command.command()+
                               "\nDescription: "+command.description()+"\n");
        }
        Footer.addFooter(builder);
        return builder;
    }

}
