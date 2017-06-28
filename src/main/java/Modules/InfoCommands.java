package Modules;

import Discord.BotUtils;
import Discord.DiscordInit;
import Events.Command;
import Events.EventListener;
import Events.Module;
import Main.MoMuOSBMain;
import Storage.ConfigDriver;
import Util.Footer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.sql.Date;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class InfoCommands extends Module {

    @Command(
            command = "help",
            description = "Display the help",
            alias = "h",
            arguments = {},
            permission = Permissions.READ_MESSAGES
    )
    public boolean help(MessageReceivedEvent event, String[] args) {
        BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), genbuildHelp());
        return true;
    }

    @Command(
            command = "invitebot",
            description = "Invites the bot",
            alias = "ib",
            arguments = {},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean inviteBot(MessageReceivedEvent event, String[] args) {
        EnumSet<Permissions> permissions = EnumSet.allOf(Permissions.class);
        BotInviteBuilder builder = new BotInviteBuilder(DiscordInit.getInstance().getDiscordClient()).withPermissions(permissions);
        BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), builder.build());
        return true;
    }

    @Command(
            command = "stats",
            description = "Display the stats",
            alias = "st",
            arguments = {},
            permission = Permissions.READ_MESSAGES
    )
    public boolean stats(MessageReceivedEvent event, String[] args) {
        BotUtils.sendEmbMessage(event.getChannel(), genbuildStats(), false);
        return true;
    }

    private EmbedBuilder genbuildStats() {
        Date now = new Date(System.currentTimeMillis());
        long diffInSeconds = (now.getTime() - MoMuOSBMain.starttime.getTime()) / 1000;

        long diff[] = new long[] { 0, 0, 0, 0 };
    /* sec */diff[3] = (diffInSeconds >= 60 ? diffInSeconds % 60 : diffInSeconds);
    /* min */diff[2] = (diffInSeconds = (diffInSeconds / 60)) >= 60 ? diffInSeconds % 60 : diffInSeconds;
    /* hours */diff[1] = (diffInSeconds = (diffInSeconds / 60)) >= 24 ? diffInSeconds % 24 : diffInSeconds;
    /* days */diff[0] = (diffInSeconds = (diffInSeconds / 24));


        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(":information_source: Generell Stats :information_source:");
        builder.withColor(Color.CYAN);
        builder.withDescription("Servercount: "+ DiscordInit.getInstance().getDiscordClient().getGuilds().size());
        builder.appendDesc("\nShardcount: "+ DiscordInit.getInstance().getDiscordClient().getShardCount());
        builder.appendDesc("\nUsers: "+ DiscordInit.getInstance().getDiscordClient().getUsers().size());
        builder.appendDesc("\nUptime: "+ String.format(
                "%d day%s, %d hour%s, %d minute%s, %d second%s",
                diff[0],
                diff[0] > 1 ? "s" : "",
                diff[1],
                diff[1] > 1 ? "s" : "",
                diff[2],
                diff[2] > 1 ? "s" : "",
                diff[3],
                diff[3] > 1 ? "s" : ""));
        builder.appendDesc("\nBotOwner: "+ DiscordInit.getInstance().getDiscordClient().getApplicationOwner().getName());
        builder.appendDesc("\nCommands: "+ EventListener.getInstance().getAllCommands().size());
        return builder;
    }

    private EmbedBuilder genbuildHelp() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(":information_source: All Commands ("+EventListener.getInstance().getAllCommands().size()+") :information_source:");
        builder.withColor(Color.CYAN);
        for (Command command : EventListener.getInstance().getAllCommands()) {
            builder.appendDesc("\nCommand:    | "+BotUtils.BOT_PREFIX+command.command()+
                                "\nAlias:              | "+BotUtils.BOT_PREFIX+command.alias()+
                                "\nArguments:   | "+ Arrays.toString(command.arguments()).replace("[", "").replace("]", "") +
                               "\nDescription:  | "+command.description()+"\n");
        }
        return builder;
    }

}
