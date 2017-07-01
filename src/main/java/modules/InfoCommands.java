package modules;

import discord.BotUtils;
import discord.DiscordInit;
import events.Command;
import events.Module;
import util.Fast;
import main.MoMuOSBMain;
import util.Prefix;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.handle.obj.StatusType;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;
import util.SMB;
import util.Utils;

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
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "help",
            description = "Display the help",
            alias = "h",
            arguments = {},
            permission = "showhelp",
            prefix = Prefix.INFO_PREFIX
    )
    public boolean help(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
            for (EmbedBuilder builder : genbuildHelp(event)) {
                BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder);
            }
        }).start();
        return true;
    }

    /**
     * Send a invitation
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "invitebot",
            description = "Invites the bot",
            alias = "ib",
            arguments = {},
            permission = "createinvite",
            prefix = Prefix.INFO_PREFIX
    )
    public boolean inviteBot(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            EnumSet<Permissions> permissions = EnumSet.allOf(Permissions.class);
            BotInviteBuilder builder = new BotInviteBuilder(INIT.BOT).withPermissions(permissions);
            BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), builder.build());
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR + LANG.getTranslation("botowner_error"), true);
        }

        return true;
    }

    /**
     * Stats Command
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "stats",
            description = "Display the stats",
            alias = "st",
            arguments = {},
            permission = "showstats",
            prefix = Prefix.INFO_PREFIX
    )
    public boolean stats(MessageReceivedEvent event, String[] args) {
        BotUtils.sendEmbMessage(event.getChannel(), genbuildStats(event), false);
        return true;
    }

    private EmbedBuilder genbuildStats(MessageReceivedEvent event) {
        Date now = new Date(System.currentTimeMillis());
        EmbedBuilder builder = new EmbedBuilder();
        builder.withColor(Color.CYAN);

        ArrayList<IUser> statusTypes = new ArrayList<>();
        event.getGuild().getUsers().forEach(iUser -> {
            if (iUser.getPresence().getStatus() != StatusType.OFFLINE) {
                statusTypes.add(iUser);
            }
        });

        String stringBuilder = LANG.getTranslation("stats_servercount") + ": " + INIT.BOT.getGuilds().size() +
                "\n" + LANG.getTranslation("stats_shards") + ": " + event.getGuild().getShard().getInfo()[0] + " / " + INIT.BOT.getShardCount() +
                "\n" + LANG.getTranslation("stats_user") + ": " + statusTypes.size() + " / " + INIT.BOT.getUsers().size() +
                "\n" + LANG.getTranslation("stats_uptime") + ": " + Utils.calculateAndFormatTimeDiff(MoMuOSBMain.starttime, now) +
                "\n" + LANG.getTranslation("stats_owner") + ": " + INIT.BOT.getApplicationOwner().getName() +
                "\n" + LANG.getTranslation("stats_commands") + ": " + COMMAND.getAllCommands().size();

        builder.appendField(":information_source: " + LANG.getTranslation("stats_title") + " :information_source:", stringBuilder, false);
        return builder;
    }

    private ArrayList<EmbedBuilder> genbuildHelp(MessageReceivedEvent event) {
        ArrayList<EmbedBuilder> builders = new ArrayList<>();
        int page = 1;
        EmbedBuilder builder = new EmbedBuilder();
        builders.add(page - 1, builder);
        builders.get(page - 1).withTitle(":information_source: " + LANG.getTranslation("help_title") + " Page: " + page + " :information_source:");
        builders.get(page - 1).withColor(Color.CYAN);
        builders.get(page - 1).withDescription(LANG.getTranslation("help_noneinfo"));
        builders.get(page - 1).appendDescription(LANG.getTranslation("help_prefixinfo"));
        for (Command command : COMMAND.getAllCommands()) {
            if (event.getAuthor().getRolesForGuild(event.getGuild()).contains(PERM.groupPermission(command.permission())) || event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
                String string = "\n" + LANG.getTranslation("help_alias") + ":               | " + command.prefix() + command.alias() +
                        "\n" + LANG.getTranslation("help_arguments") + ":   | " + Arrays.toString(command.arguments()).replace("[", "").replace("]", "") +
                        "\n" + LANG.getTranslation("help_description") + ":   | " + LANG.getMethodDescription(command)+
                        "\n" + LANG.getTranslation("help_permission") + ":   | " + command.permission() + "\n";
                builders.get(page - 1).appendField(LANG.getTranslation("help_command") + "       | " + command.prefix() + command.command(), string, false);
            }
            if (builders.get(page - 1).getFieldCount() == EmbedBuilder.FIELD_COUNT_LIMIT) {
                EmbedBuilder buildertemp = new EmbedBuilder();
                builders.add(page - 1, buildertemp);
                page++;
            }
        }

        return builders;
    }

}
