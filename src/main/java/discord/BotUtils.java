package discord;

import main.Fast;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.obj.*;
import util.Console;
import util.Footer;
import sx.blah.discord.api.ClientBuilder;
import sx.blah.discord.api.IDiscordClient;
import sx.blah.discord.util.DiscordException;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.MessageHistory;
import sx.blah.discord.util.RequestBuffer;

import java.awt.*;
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
    public static void sendMessage(IChannel channel, String message, boolean delete){
        RequestBuffer.request(() -> {
            try{
                if (channel.getModifiedPermissions(INIT.BOT.getOurUser()).contains(Permissions.SEND_MESSAGES)) {
                    channel.sendMessage(message);
                    deleteMessageFromBot(channel, delete);
                } else {
                    Console.error(String.format(LANG.getTranslation("notsendpermission_error"), channel.getGuild().getName(), channel.getName()));
                }
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            }
        });

    }
    /**
     * Send a Message in Private Channel
     * @param userchannel Channel
     * @param message Message
     */
    public static void sendPrivMessage(IPrivateChannel userchannel, String message){
        RequestBuffer.request(() -> {
            try{
                userchannel.sendMessage(message);
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            }
        });

    }
    /**
     * Send a Message in Channel
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     * @param delete deletion after time
     */
    public static void sendEmbMessage(IChannel channel, EmbedBuilder builder, boolean delete){
        RequestBuffer.request(() -> {
            try{
                if (channel.getModifiedPermissions(INIT.BOT.getOurUser()).contains(Permissions.SEND_MESSAGES)) {
                    Footer.addFooter(builder);
                    channel.sendMessage(builder.build());
                    deleteMessageFromBot(channel, delete);
                } else {
                    Console.error(String.format(LANG.getTranslation("notsendpermission_error"), channel.getGuild().getName(), channel.getName()));
                }
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            }
        });

    }
    /**
     * Send a Message in Channel
     * @param channel Channel
     * @param builder Message as Embeded Builder Instance
     */
    public static void sendPrivEmbMessage(IPrivateChannel channel, EmbedBuilder builder){
        RequestBuffer.request(() -> {
            try{
                Footer.addFooter(builder);
                channel.sendMessage(builder.build());
            } catch (DiscordException e){
                Console.error(String.format(LANG.getTranslation("notsend_error"), e.getMessage()));
            }
        });

    }

    /**
     * Delte the Message of the Bot after time
     * @param channel Channel
     * @param delete delete
     */
    private static void deleteMessageFromBot(IChannel channel, boolean delete) {
        if (delete && DRIVER.getPropertyOnly(DRIVER.CONFIG,"deleteBotAnswers").equals(true)) {
            new Thread(() -> {
                try {
                    Thread.sleep(Integer.parseInt(DRIVER.getPropertyOnly(DRIVER.CONFIG,"botanswerdeletseconds").toString()) * 1000);
                    MessageHistory messages = channel.getMessageHistory(10);
                    for (IMessage message: messages) {
                        if (message.getAuthor().isBot()) {
                            for (IEmbed obj: message.getEmbeds()) {
                                if (obj.getColor().equals(Color.green)) {
                                    if (!message.isDeleted()) {
                                        message.delete();
                                    }
                                }
                            }
                            break;
                        }
                    }
                } catch (Exception ex) {
                    Console.error(LANG.getTranslation("common_error"));
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
                channel.bulkDelete(messages);
            } catch (DiscordException e) {
                Console.error(String.format(LANG.getTranslation("notdeleted_error"), e.getMessage()));
            }
        });
    }
}
