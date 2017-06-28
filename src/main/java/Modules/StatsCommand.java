package Modules;

import Discord.BotUtils;
import Discord.DiscordInit;
import Events.Command;
import Events.Module;
import Main.MoMuOSBMain;
import Util.Footer;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.sql.Date;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class StatsCommand extends Module {
    public static Module instance;

    @Command(
            command = "stats",
            description = "Display the stats",
            alias = "st",
            permission = Permissions.READ_MESSAGES
    )
    public boolean stats(MessageReceivedEvent event, String[] args) {
        BotUtils.sendEmbMessage(event.getChannel(), genbuild().build());
        return true;
    }
    public EmbedBuilder genbuild() {
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
        Footer.addFooter(builder);
        return builder;
    }
}
