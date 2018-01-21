package modules;

import discord.BotUtils;
import discord.SystemInfo;
import events.Command;
import events.Module;
import main.MoMuOSBMain;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.BotInviteBuilder;
import sx.blah.discord.util.EmbedBuilder;
import util.*;

import java.awt.*;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Locale;
import java.util.concurrent.Future;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
//GITHUB IDENT MODDYLP
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
        genbuildHelp(event);
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
            command = "zitat",
            description = "Create a Quote in this channel",
            alias = "zt",
            arguments = {"Game", "Date", "Quote[]"},
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean zitat(MessageReceivedEvent event, String[] args) {
        try {
            EmbedBuilder builder = new EmbedBuilder();
            builder.withColor(Color.YELLOW);
            if (args.length >= 3) {
                DateFormat format = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
                java.util.Date date = format.parse(args[1]);
                System.out.println(date);
                builder.withTitle("Quote from __"
                        + (event.getAuthor().getNicknameForGuild(event.getGuild()) != null ? event.getAuthor().getNicknameForGuild(event.getGuild()) : event.getAuthor().getName() )
                        + "__ at __"
                        +format.format(date)
                        +"__ in the Game __"
                        +args[0]+
                        "__");
                builder.withDesc("---------------------------------------------------------\n"
                        +Utils.makeArgsToString(args, new String[]{args[0], args[1]})+
                        "\n---------------------------------------------------------");
                BotUtils.sendEmbMessage(event.getChannel(), builder, false);
            } else {
                throw new Exception("To few Arguments");
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("A error occurred: "+ex.getMessage()), true);
            Console.error(ex);
        }
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
            command = "userinfo",
            description = "Display Infos about a User",
            alias = "usri",
            arguments = {"Mention Users[]"},
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean userinfo(MessageReceivedEvent event, String[] args) {
        final StringBuilder stringBuilder = new StringBuilder();
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        new Thread(() -> {
            try {
                for (IUser user : event.getMessage().getMentions()) {
                    ArrayList<IGuild> userjoinedguilds = new ArrayList<>();
                    for (IGuild guild : Utils.getServerbyUser(user)) {
                            userjoinedguilds.add(guild);
                            if (user.getNicknameForGuild(guild) != null && !user.getNicknameForGuild(guild).isEmpty()) {
                                stringBuilder.append(user.getNicknameForGuild(guild)).append("\n");
                            }
                    }
                    if (stringBuilder.length() == 0) {
                        stringBuilder.append("No Nicknames");
                    }
                    EmbedBuilder builder = new EmbedBuilder();
                    builder.withColor(Color.cyan);
                    builder.withTitle("UserInfo for " + user.getName());
                    builder.appendField("Signed Up", user.getCreationDate().format(DateTimeFormatter.ofPattern("dd.MM.yyyy")), true);
                    builder.appendField("Shared Servers", String.valueOf(userjoinedguilds.size()), true);
                    builder.appendField("Nicknames", stringBuilder.toString(), false);
                    builder.withThumbnail(user.getAvatarURL());
                    StringBuilder builder1 = new StringBuilder();
                    for (IGuild guild : Utils.getServerbyUser(user)) {
                        builder1.append(guild.getName()).append("  ").append("  ").append(guild.getRegion().getName()).append("\n");
                    }
                    builder.appendField("Server", builder1.toString(), false);

                    IMessage message = BotUtils.sendEmbMessage(event.getChannel(), builder, false);
                    BotUtils.addReactionToMessage(message, "x");
                }
                if (event.getMessage().getMentions().size() == 0) {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("You should mention at least one User"), true);
                }
            } catch (Exception ex) {
                Console.error(ex);
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("Something went wrong"), true);
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
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean inviteBot(MessageReceivedEvent event, String[] args) {
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        EnumSet<Permissions> permissions = EnumSet.allOf(Permissions.class);
        BotInviteBuilder builder = new BotInviteBuilder(INIT.BOT).withPermissions(permissions);
        IMessage message = BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), builder.build(), false);
        BotUtils.addReactionToMessage(message, "x");
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
            IChannel channel = INIT.BOT.getGuildByID(Long.valueOf(args[0])).getDefaultChannel();
            if (channel == null) {
                channel = INIT.BOT.getGuildByID(Long.valueOf(args[0])).getChannels().get(0);
            }
            if (channel != null) {
                IInvite invite = channel.createInvite(0, 1, false, false);
                if (invite != null) {
                    IMessage message = BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), invite.toString() + "   ||||   " + invite.getCode(), false);
                    BotUtils.addReactionToMessage(message, "x");
                } else {
                    BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "The Bot cant create a InviteLink", true);
                }
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
            } else {
                BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "No Channel found", true);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * Get the Owner
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "getOwner",
            description = "Get the Server Owner",
            alias = "getOw",
            arguments = {"Server ID"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean getOwner(MessageReceivedEvent event, String[] args) {
        IGuild guild = INIT.BOT.getGuildByID(Long.valueOf(args[0]));
        if (guild != null) {
            IUser user = guild.getOwner();
            if (user != null) {
                IMessage message = BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "Owner of Server(" + guild.getName() + ") is " +user.getName()+"  ("+user.getStringID()+") "+ user.mention(), false);
                BotUtils.addReactionToMessage(message, "x");
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
            } else {
                BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "No Channel found", true);
                return false;
            }
            return true;
        } else {
            return false;
        }
    }

    @Command(
            command = "ping",
            description = "Ping the Bot",
            alias = "pong",
            arguments = {},
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean ping(MessageReceivedEvent event, String[] args) {
        IMessage message = BotUtils.sendMessage(event.getChannel(), "Pinging...", false);
        if (message != null) {
            BotUtils.updateMessage(message, event.getMessage().getCreationDate().until(message.getCreationDate(), ChronoUnit.MILLIS) + "ms | Websocket: " + event.getGuild().getShard().getResponseTime() + "ms");
            BotUtils.addReactionToMessage(message, "x");
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
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean stats(MessageReceivedEvent event, String[] args) {
        IMessage message = BotUtils.sendEmbMessage(event.getChannel(), genbuildStats(event), false);
        BotUtils.addReactionToMessage(message, "x");
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

    private void genbuildHelp(MessageReceivedEvent event) {
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success_wait")), true);
        Future<ArrayList<EmbedBuilder>> task = BotUtils.generateHelp(event);
        try {
            for (EmbedBuilder builder : task.get()) {
                IMessage message = BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder, false);
                BotUtils.addReactionToMessage(message, "x");
            }
        } catch (Exception ex) {
            Console.error(ex);
        }
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
        DRIVER.setProperty(DEF_LANG, "help_title", "You have access to these Commands: ");
        DRIVER.setProperty(DEF_LANG, "help_page", "Page");
        DRIVER.setProperty(DEF_LANG, "help_command", "Command");
        DRIVER.setProperty(DEF_LANG, "help_alias", "Alias");
        DRIVER.setProperty(DEF_LANG, "help_arguments", "Arguments");
        DRIVER.setProperty(DEF_LANG, "help_description", "Description");
        DRIVER.setProperty(DEF_LANG, "help_noneinfo", "**__If you want to reset a Value, then type for each argument NA.__**");
        DRIVER.setProperty(DEF_LANG, "help_prefixinfo", "\nThe Prefixes are \n" +
                "**Admin Prefix**:   !   \n" +
                "**Info Prefix**:    .   \n" +
                "**Game Prefix**:    ~   \n" +
                "**Music Prefix**:   $   \n");
        DRIVER.setProperty(DEF_LANG, "help_permission", "Permission");

    }
}
