package discord;

import sx.blah.discord.handle.impl.obj.Message;
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

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class BotUtils implements Fast{

    /**
     * Creates a Bot Instance
     * @param token the Auth Token
     * @return the BotInstance
     */

    static IDiscordClient getBuiltDiscordClient(String token){
        // The ClientBuilder object is where you will attach your params for configuring the instance of your bot.
        // Such as withToken, setDaemon etc
        return new ClientBuilder()
                .withRecommendedShardCount()
                .withToken(token)
                .build();

    }

    /**
     * Send a Message in Channel
     * @param channel Channel
     * @param message Message
     * @param delete deletion after time
     */
    public static IMessage sendMessage(IChannel channel, String message, boolean delete){
        RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
            try{
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
                    Console.debug("BOT is not on this server: "+channel.getGuild().getName());
                    return null;
                }
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
                return null;
            }
        });
        return feature.get();
    }
    /**
     * Send a Message in Private Channel
     * @param userchannel Channel
     * @param message Message
     */
    public static IMessage sendPrivMessage(IPrivateChannel userchannel, String message, boolean delete){
        RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
            try{
                IMessage messages = userchannel.sendMessage(message);
                deleteMessageFromBot(messages, delete);
                return messages;
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
                return null;
            }
        });
        return feature.get();

    }
    /**
     * Send a Message in Channel
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     * @param delete deletion after time
     */
    public static IMessage sendEmbMessage(IChannel channel, EmbedBuilder builder, boolean delete){
        RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
            try{
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
                    Console.debug("BOT is not on this server: "+channel.getGuild().getName());
                    return null;
                }
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
                return null;
            }
        });
        return feature.get();
    }
    /**
     * Send a Message in Channel
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     */
    public static IMessage sendPrivEmbMessage(IPrivateChannel channel, EmbedBuilder builder, boolean delete){
        RequestBuffer.RequestFuture<IMessage> feature = RequestBuffer.request(() -> {
            try{
                Footer.addFooter(builder);
                IMessage message = channel.sendMessage(builder.build());
                deleteMessageFromBot(message, delete);
                return message;
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
                return null;
            }
        });
        return feature.get();

    }

    /**
     * Delte the Message of the Bot after time
     * @param message Channel
     * @param delete delete
     */
    private static void deleteMessageFromBot(IMessage message, boolean delete) {
        if (delete && DRIVER.getPropertyOnly(DRIVER.CONFIG,"deleteBotAnswers").equals(true)) {
            new Thread(() -> {
                try {
                    if (INIT.BOT.getGuildByID(message.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                        Thread.sleep(Integer.parseInt(DRIVER.getPropertyOnly(DRIVER.CONFIG, "botanswerdeletseconds").toString()) * 1000);
                        message.delete();
                    } else {
                        Console.debug("BOT is not on this server: "+message.getGuild().getName());
                    }
                } catch (Exception ex) {
                    Console.error(String.format(LANG.getTranslation("commonmessage_error"),ex.getMessage()+"  "+ex.getCause().getMessage()));
                }
            }).start();
        }
    }

    /**
     * Delete One message
     * @param message message
     */
    public static void deleteMessageOne(IMessage message) {
        RequestBuffer.request(() -> {
            try {
                message.delete();
            } catch (DiscordException e) {
                Console.error(String.format(LANG.getTranslation("notdeleted_error"), e.getMessage()));
            }
        });
    }

    /**
     * Deletes a List of messages
     * @param channel channel
     * @param messages messages
     */
    public static void bulkdeleteMessage(IChannel channel, List<IMessage> messages) {
        RequestBuffer.request(() -> {
            try {
                if (INIT.BOT.getGuildByID(channel.getGuild().getLongID()).getUsers().contains(INIT.BOT.getOurUser())) {
                    channel.bulkDelete(messages);
                } else {
                    Console.debug("BOT is not on this server: "+channel.getGuild().getName());
                }
            } catch (DiscordException e) {
                Console.error(String.format(LANG.getTranslation("notdeleted_error"), e.getMessage()));
            }
        });
    }
}
