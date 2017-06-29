package modules;

import discord.BotUtils;
import discord.DiscordInit;
import events.Command;
import events.Module;
import main.Fast;
import main.MoMuOSBMain;
import util.Utils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class InfoCommands extends Module implements Fast {

    /**
     * Help Command
     * @param event MessageEvent
     * @param args Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "help",
            description = "Display the help",
            alias = "h",
            arguments = {},
            permission = Permissions.READ_MESSAGES
    )
    public boolean help(MessageReceivedEvent event, String[] args) {
        for (EmbedBuilder builder: genbuildHelp()) {
            BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder);
        }
        return true;
    }

    /**
     * Send a invitation
     * @param event MessageEvent
     * @param args Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "invitebot",
            description = "Invites the bot",
            alias = "ib",
            arguments = {},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean inviteBot(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            EnumSet<Permissions> permissions = EnumSet.allOf(Permissions.class);
            BotInviteBuilder builder = new BotInviteBuilder(INIT.BOT).withPermissions(permissions);
            BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), builder.build());
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("botowner_error"), true);
        }

        return true;
    }

    /**
     * Stats Command
     * @param event MessageEvent
     * @param args Argumente [Not needed]
     * @return state
     */
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



        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle(":information_source: "+LANG.getTranslation("stats_title")+" :information_source:");
        builder.withColor(Color.CYAN);
        String stringBuilder = LANG.getTranslation("stats_servercount") + ": " + INIT.BOT.getGuilds().size() +
                "\n" + LANG.getTranslation("stats_shards") + ": " + INIT.BOT.getShardCount() +
                "\n" + LANG.getTranslation("stats_user") + ": " + INIT.BOT.getUsers().size() +
                "\n" + LANG.getTranslation("stats_uptime") + ": " + Utils.calculateAndFormatTimeDiff(MoMuOSBMain.starttime, now) +
                "\n" + LANG.getTranslation("stats_owner") + ": " + INIT.BOT.getApplicationOwner().getName() +
                "\n" + LANG.getTranslation("stats_commands") + ": " + EVENT.getAllCommands().size();

        builder.appendField("", stringBuilder, false);
        return builder;
    }

    private ArrayList<EmbedBuilder> genbuildHelp() {
        ArrayList<EmbedBuilder> builders = new ArrayList<>();
        int page = 1;
        EmbedBuilder builder = new EmbedBuilder();
        builders.add(page-1, builder);
        builders.get(page-1).withTitle(":information_source: "+ LANG.getTranslation("help_title")+" ("+EVENT.getAllCommands().size()+") Page: "+page+" :information_source:");
        builders.get(page-1).withColor(Color.CYAN);
        for (Command command : EVENT.getAllCommands()) {
            String string = "\n"+LANG.getTranslation("help_alias")+":              | "+BotUtils.BOT_PREFIX+command.alias()+
                                "\n"+LANG.getTranslation("help_arguments")+":   | "+ Arrays.toString(command.arguments()).replace("[", "").replace("]", "") +
                                "\n"+LANG.getTranslation("help_description")+":  | "+command.description()+"\n";
            builders.get(page-1).appendField(LANG.getTranslation("help_command")+"    | "+BotUtils.BOT_PREFIX+command.command(), string, false);
            if (builders.get(page-1).getFieldCount() ==  EmbedBuilder.FIELD_COUNT_LIMIT) {
                EmbedBuilder buildertemp = new EmbedBuilder();
                builders.add(page-1, buildertemp);
                page++;
            }
        }

        return builders;
    }

}
