package discord;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.vdurmont.emoji.EmojiManager;
import events.Command;
import events.RegisterCommands;
import events.ServerListener;
import javafx.concurrent.Task;
import modules.RoleManagement;
import modules.music.MainMusic;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.obj.Message;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import util.Fast;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.handle.obj.*;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.RequestBuffer;
import util.Console;
import util.Footer;
import util.Utils;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class BotUtils implements Fast {

    /**
     * Creates a Bot Instance
     *
     * @param token the Auth Token
     * @return the BotInstance
     */

    static IDiscordClient getBuiltDiscordClient(String token) {
        // The ClientBuilder object is where you will attach your params for configuring the instance of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withRecommendedShardCount()
                .withToken(token)
                .build();

    }

    /**
     * Send a Message in Channel
     *
     * @param channel Channel
     * @param message Message
     * @param delete  deletion after time
     */
    public static IMessage sendMessage(IChannel channel, String message, boolean delete) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
                if (channel.isPrivate()) {
                    Console.error("Message not send. Try to send public message to private Channel" + message);
                    return null;
                }
                if (INIT.BOT.getGuildByID(channel.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    if (channel.getModifiedPermissions(INIT.BOT.getOurUser()).contains(Permissions.SEND_MESSAGES)) {
                        IMessage messageinst = channel.sendMessage(message);
                        deleteMessageFromBot(messageinst, delete);
                        return messageinst;
                    } else {
                        Console.error(String.format(LANG.getTranslation("notsendpermission_error"), channel.getGuild().getName(), channel.getName()));
                        return null;
                    }
                } else {
                    Console.debug("BOT is not on this server: " + channel.getGuild().getName());
                    return null;
                }

            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }
    }
    public static IMessage updateMessage(IMessage message, String text) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
                if (INIT.BOT.getGuildByID(message.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    return message.edit(text);
                } else {
                    Console.debug("BOT is not on this server: " + message.getGuild().getName());
                    return null;
                }

            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }
    }

    /**
     * Send a Message in Private Channel
     *
     * @param userchannel Channel
     * @param message     Message
     */
    public static IMessage sendPrivMessage(IPrivateChannel userchannel, String message, boolean delete) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {

                IMessage messages = userchannel.sendMessage(message);
                deleteMessageFromBot(messages, delete);
                return messages;

            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }

    }

    /**
     * Send a Message in Channel
     *
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     * @param delete  deletion after time
     */
    public static IMessage sendEmbMessage(IChannel channel, EmbedBuilder builder, boolean delete) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {

                if (INIT.BOT.getGuildByID(channel.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    if (channel.getModifiedPermissions(INIT.BOT.getOurUser()).contains(Permissions.SEND_MESSAGES)) {
                        Footer.addFooter(builder);
                        IMessage message = channel.sendMessage(builder.build());
                        deleteMessageFromBot(message, delete);
                        return message;
                    } else {
                        Console.error(String.format(LANG.getTranslation("notsendpermission_error"), channel.getGuild().getName(), channel.getName()));
                        return null;
                    }
                } else {
                    Console.debug("BOT is not on this server: " + channel.getGuild().getName());
                    return null;
                }

            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }
    }

    public static IMessage updateEmbMessage(IChannel channel, EmbedBuilder builder, IMessage message) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
                if (INIT.BOT.getGuildByID(channel.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    if (channel.getModifiedPermissions(INIT.BOT.getOurUser()).contains(Permissions.SEND_MESSAGES)) {
                        return message.edit(builder.build());
                    } else {
                        Console.error(String.format(LANG.getTranslation("notsendpermission_error"), channel.getGuild().getName(), channel.getName()));
                        return null;
                    }
                } else {
                    Console.debug("BOT is not on this server: " + channel.getGuild().getName());
                    return null;
                }
            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }
    }

    /**
     * Send a Message in Channel
     *
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     */
    public static IMessage sendPrivEmbMessage(IPrivateChannel channel, EmbedBuilder builder, boolean delete) {
        try {
            RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
                Footer.addFooter(builder);
                IMessage message = channel.sendMessage(builder.build());
                deleteMessageFromBot(message, delete);
                return message;

            });
            return feature.get();
        } catch (DiscordException e) {
            Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            return null;
        }


    }

    /**
     * Delte the Message of the Bot after time
     *
     * @param message Channel
     * @param delete  delete
     */
    private static void deleteMessageFromBot(IMessage message, boolean delete) {
        try {
            if (delete && DRIVER.getPropertyOnly(DRIVER.CONFIG, "deleteBotAnswers").equals(true)) {
                new Thread(() -> {
                    try {
                        Thread.sleep(Integer.parseInt(DRIVER.getPropertyOnly(DRIVER.CONFIG, "botanswerdeletseconds").toString()) * 1000);
                    } catch (InterruptedException e) {
                        Console.error(e);
                    }
                    RequestBuffer.RequestFuture<Void> feature = RequestBuffer.request(message::delete);

                }).start();
            }
        } catch (Exception ex) {
            Console.error("BOTDelete: " + String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage()));
        }
    }

    /**
     * Delete One message
     *
     * @param message message
     */
    public static void deleteMessageOne(IMessage message) {
        try {
            RequestBuffer.request(message::delete);
        } catch (Exception e) {
            Console.error("OneMSGdelete: " + String.format(LANG.getTranslation("notdeleted_error"), e.getMessage()));
        }
    }

    /**
     * Deletes a List of messages
     *
     * @param channel  channel
     * @param messages messages
     */
    public static void bulkdeleteMessage(IChannel channel, List<IMessage> messages) {
        try {
            RequestBuffer.request(() -> {

                if (INIT.BOT.getGuildByID(channel.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    channel.bulkDelete(messages);
                } else {
                    Console.debug("BOT is not on this server: " + channel.getGuild().getName());
                }

            });
        } catch (DiscordException e) {
            Console.error("BulkDelete: " + String.format(LANG.getTranslation("notdeleted_error"), e.getMessage()));
        }
    }

    public static void addReactionToMessage(IMessage message, String emoji) {
        try {
            RequestBuffer.request(() -> {
                message.addReaction(EmojiManager.getForAlias(emoji));
            });
        } catch (Exception ex) {
            Console.error("Reaction not added(OUTTER): " + ex.getMessage());
        }
    }

    public static void initBot() {
        Console.println("Bot login success");
        Console.println("Shards: " + INIT.BOT.getShardCount());
        StringBuilder serverstr = new StringBuilder();
        int count = 1;
        for (IGuild server : INIT.BOT.getGuilds()) {
            serverstr.append("\n")
                    .append(count)
                    .append(". [")
                    .append(server.getName())
                    .append("   ")
                    .append(server.getStringID())
                    .append("]");
            count++;
        }
        Console.println("Servers: " + serverstr);
        RegisterCommands.registerAll();
        Console.println("Loading Permissions from SaveFile");
        PERM.loadPermissions(INIT.BOT.getGuilds());
        PERM.setDefaultPermissions(INIT.BOT.getGuilds(), false);
        INIT.BOT.changePlayingText(DRIVER.getPropertyOnly(DRIVER.CONFIG, "defaultplaying").toString());
        INIT.BOT.changeUsername(DRIVER.getPropertyOnly(DRIVER.CONFIG, "defaultUsername").toString());
        Console.println("Loading Command Descriptions");
        for (Command command : COMMAND.getAllCommands()) {
            LANG.getMethodDescription(command);
        }
        Console.println("Register Audioprovider");
        AudioSourceManagers.registerRemoteSources(MainMusic.playerManager);
        AudioSourceManagers.registerLocalSource(MainMusic.playerManager);

        RoleManagement.loadGenders();

        ServerListener.getInstance().saveGuilds();
        Console.println("====================================Bot Status========================================");
        INIT.BOT.getShards().forEach(iShard -> {
            Console.println("Shard " + iShard.getInfo()[0] + ": " + iShard.isReady() + " Servers: " + iShard.getGuilds().size() + "  Ping: " + iShard.getResponseTime());
        });
        Optional stream = INIT.BOT.getOurUser().getPresence().getStreamingUrl();
        Optional playtext = INIT.BOT.getOurUser().getPresence().getPlayingText();
        Console.println("Username: " + INIT.BOT.getOurUser().getName());
        Console.println("Status: " + INIT.BOT.getOurUser().getPresence().getStatus());
        if (stream.isPresent()) {
            Console.println("Streaming: " + stream.get());
        } else {
            Console.println("Streaming: OFFLINE");
        }
        if (playtext.isPresent()) {
            Console.println("Playing: " + playtext.get());
        } else {
            Console.println("Playing: NOTHING");
        }

        SystemInfo info = new SystemInfo();
        Console.println("SystemInfo: " + info.Info() + "\n");
        Command helpcommand = COMMAND.getCommandByName("help");
        Console.println("Type " + DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString() + helpcommand.prefix() + helpcommand.command() + " for getting help.");
        Console.println("====================================Bot Start completed===============================");
        ServerListener.running = true;
    }

    public static Future<ArrayList<EmbedBuilder>> generateHelp(MessageReceivedEvent event) {
        Callable<ArrayList<EmbedBuilder>> task = () -> {
            ArrayList<EmbedBuilder> builders = new ArrayList<>();
            int page = 1;
            String botprefix = DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString();
            EmbedBuilder builder = new EmbedBuilder();
            builders.add(page - 1, builder);
            builders.get(page - 1).withDescription(LANG.getTranslation("help_noneinfo"));
            builders.get(page - 1).appendDescription(LANG.getTranslation("help_prefixinfo"));
            builders.get(page - 1).withColor(Color.CYAN);
            int count = 0;
            for (Command command : COMMAND.getAllCommands()) {
                if (PERM.hasPermission(event.getAuthor(), event.getGuild(), command.permission())) {
                    String string = "\n**" + LANG.getTranslation("help_alias") + "**:               = " + botprefix + command.prefix() + command.alias() +
                            "\n**" + LANG.getTranslation("help_arguments") + "**:  = " + Arrays.toString(command.arguments()).replace("[", "").replace("]", "") +
                            "\n**" + LANG.getTranslation("help_description") + "**: = " + LANG.getMethodDescription(command) +
                            "\n**" + LANG.getTranslation("help_permission") + "**:  = " + command.permission() + "\n";
                    builders.get(page - 1).appendField((count + 1) + ".  " + botprefix + command.prefix() + command.command(), string, false);
                }
                page = Utils.checkIfEmbedisToBig(builders, page, ":information_source: " + LANG.getTranslation("help_title") + " Page: " + page + " :information_source:");
                count++;
            }
            builders.get(0).withTitle(":information_source: " + LANG.getTranslation("help_title") + "(" + count + " / " + COMMAND.getAllCommands().size() + ")" + LANG.getTranslation("help_page") + 1 + " :information_source:");
            return builders;
        };


        ExecutorService executor = Executors.newFixedThreadPool(1);
        Future<ArrayList<EmbedBuilder>> future = executor.submit(task);
        System.out.println("future done? " + future.isDone());
        return future;
    }


}
