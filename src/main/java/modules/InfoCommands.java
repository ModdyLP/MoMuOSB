package modules;

import discord.BotUtils;
import discord.DiscordInit;
import discord.SystemInfo;
import events.Command;
import events.Module;
import permission.PermissionController;
import storage.LanguageMethod;
import sx.blah.discord.handle.obj.*;
import util.*;
import main.MoMuOSBMain;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;

import java.awt.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
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
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean help(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success_wait")), true);
            for (EmbedBuilder builder : genbuildHelp(event)) {
                BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder, false);
            }
        }).start();
        return true;
    }

    /**
     * Help Command
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "getregister",
            description = "Display the help",
            alias = "greg",
            arguments = {"Mention User []"},
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean getRegisterDate(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            StringBuilder content = new StringBuilder();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy H:m:s");
            if (event.getMessage().getMentions().size() > 0) {
                for (IUser user : event.getMessage().getMentions()) {
                    content.append(user.getName()).append(":  ").append(user.getCreationDate().format(formatter)).append("\n");
                }
            } else {
                content.append("No User specified");
            }
            BotUtils.sendMessage(event.getChannel(), content.toString(), false);
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
            permission = Globals.BOT_MANAGE,
            prefix = Globals.INFO_PREFIX
    )
    public boolean inviteBot(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
            EnumSet<Permissions> permissions = EnumSet.allOf(Permissions.class);
            BotInviteBuilder builder = new BotInviteBuilder(INIT.BOT).withPermissions(permissions);
            BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), builder.build(), false);
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR + LANG.getTranslation("botowner_error"), true);
        }

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
            command = "createInvite",
            description = "Invites the bot",
            alias = "crei",
            arguments = {"Server ID"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean createInvite(MessageReceivedEvent event, String[] args) {
        Console.debug("Creating Invite link to Server: " + args[0]);
        if (INIT.BOT.getGuildByID(Long.valueOf(args[0])) != null) {
            IChannel channel = INIT.BOT.getGuildByID(Long.valueOf(args[0])).getGeneralChannel();
            if (channel == null) {
                channel = INIT.BOT.getGuildByID(Long.valueOf(args[0])).getChannels().get(0);
            }
            if (channel != null) {
                IInvite invite = channel.createInvite(0, 1, false, false);
                if (invite != null) {
                    BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), invite.toString() + "   ||||   " + invite.getCode(), false);
                } else {
                    BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "The Bot cant create a InviteLink", false);
                }
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
            } else {
                BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "No Channel found", false);
                return false;
            }
            return true;
        } else {
            return false;
        }
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
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
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
        INIT.BOT.getGuilds().forEach(iGuild -> iGuild.getUsers().forEach(iUser -> {
            if (iUser.getPresence().getStatus() != StatusType.OFFLINE) {
                statusTypes.add(iUser);
            }
        }));
        SystemInfo info = new SystemInfo();
        String stringBuilder = LANG.getTranslation("stats_servercount") + ": " + INIT.BOT.getGuilds().size() +
                "\n" + LANG.getTranslation("stats_shards") + ": " + event.getGuild().getShard().getInfo()[0] + " / " + INIT.BOT.getShardCount() +
                "\n" + LANG.getTranslation("stats_shard_ping") + ": " + event.getGuild().getShard().getResponseTime() +
                "\n" + LANG.getTranslation("stats_ram") + ": " + info.getUsedMem() +
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
        String botprefix = DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString();
        EmbedBuilder builder = new EmbedBuilder();
        builders.add(page - 1, builder);
        builders.get(page - 1).withDescription(LANG.getTranslation("help_noneinfo"));
        builders.get(page - 1).appendDescription(LANG.getTranslation("help_prefixinfo"));
        int count = 0;
        for (Command command : COMMAND.getAllCommands()) {
            if (PERM.hasPermission(event.getAuthor(), event.getGuild(), command.permission())) {
                String string = "\n" + LANG.getTranslation("help_alias") + ":               | " + botprefix + command.prefix() + command.alias() +
                        "\n" + LANG.getTranslation("help_arguments") + ":   | " + Arrays.toString(command.arguments()).replace("[", "").replace("]", "") +
                        "\n" + LANG.getTranslation("help_description") + ":   | " + LANG.getMethodDescription(command) +
                        "\n" + LANG.getTranslation("help_permission") + ":   | " + command.permission() + "\n";
                builders.get(page - 1).appendField((count + 1) + ". " + LANG.getTranslation("help_command") + "       | " + botprefix + command.prefix() + command.command(), string, false);
            }
            if (builders.get(page - 1).getFieldCount() >= EmbedBuilder.FIELD_COUNT_LIMIT || builders.get(page - 1).getTotalVisibleCharacters() >= (EmbedBuilder.MAX_CHAR_LIMIT - 1000)) {
                page++;
                EmbedBuilder buildertemp = new EmbedBuilder();
                builders.add(page - 1, buildertemp);
                builders.get(page - 1).withTitle(":information_source: " + LANG.getTranslation("help_title") + " Page: " + page + " :information_source:");
                builders.get(page - 1).withColor(Color.CYAN);
            }
            count++;
        }
        builders.get(0).withTitle(":information_source: " + LANG.getTranslation("help_title") + "(" + count + ")" + LANG.getTranslation("help_page") + 1 + " :information_source:");
        return builders;
    }

    @LanguageMethod(
            languagestringcount = 18
    )
    @Override
    public void setdefaultLanguage() {
        //Stats Command
        DRIVER.setProperty(DEF_LANG, "stats_title", "General Stats");
        DRIVER.setProperty(DEF_LANG, "stats_servercount", "Server Count");
        DRIVER.setProperty(DEF_LANG, "stats_shards", "Shards");
        DRIVER.setProperty(DEF_LANG, "stats_owner", "Bot Owner");
        DRIVER.setProperty(DEF_LANG, "stats_user", "Users");
        DRIVER.setProperty(DEF_LANG, "stats_commands", "Commands");
        DRIVER.setProperty(DEF_LANG, "stats_uptime", "Uptime");
        DRIVER.setProperty(DEF_LANG, "stats_ram", "Ram Usage");
        DRIVER.setProperty(DEF_LANG, "stats_shard_ping", "Ping for Shard");

        //Help Command
        DRIVER.setProperty(DEF_LANG, "help_title", "All Commands");
        DRIVER.setProperty(DEF_LANG, "help_page", "Page");
        DRIVER.setProperty(DEF_LANG, "help_command", "Command");
        DRIVER.setProperty(DEF_LANG, "help_alias", "Alias");
        DRIVER.setProperty(DEF_LANG, "help_arguments", "Arguments");
        DRIVER.setProperty(DEF_LANG, "help_description", "Description");
        DRIVER.setProperty(DEF_LANG, "help_noneinfo", "If you want to reset a Value, then type for each argument NA.");
        DRIVER.setProperty(DEF_LANG, "help_prefixinfo", "\nThe Prefixes are \n" +
                "Admin Prefix:   !   \n" +
                "Info Prefix:    .   \n" +
                "Game Prefix:    ~   \n" +
                "music Prefix:   $   \n");
        DRIVER.setProperty(DEF_LANG, "help_permission", "Permission");

    }
}
