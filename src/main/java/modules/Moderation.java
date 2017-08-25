package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import storage.LanguageInterface;
import storage.LanguageMethod;
import sx.blah.discord.handle.obj.IPrivateChannel;
import util.Fast;
import util.Globals;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.MessageHistory;
import util.Console;
import util.SMB;

import java.util.List;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Moderation extends Module implements Fast {

    /**
     * Deletes a List Message
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "deletemessages",
            description = "Deletion of Message Amount",
            alias = "dm",
            arguments = {"Count"},
            permission = Globals.BOT_MANAGE,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean deleteMessages(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            int total = 0;
            try {
                IChannel channel = event.getChannel();
                MessageHistory messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                BotUtils.bulkdeleteMessage(channel, messages);
                if (messages.getLatestMessage().getLongID() == event.getMessage().getLongID()) {
                    while (!event.getMessage().isDeleted()) {
                        Thread.sleep(200);
                    }
                    messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                }
                total = messages.size();

                Thread.sleep(2000);
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("del_wait_success")), true);
            } catch (Exception ex) {
                BotUtils.sendMessage(event.getChannel(), LANG.ERROR + String.format(LANG.getTranslation("deletion_error"), total, total, ex.getMessage()), true);
            }
        }).start();

        return true;
    }

    /**
     * Force Deletes a List Message
     *
     * @param event MessageEvent
     * @param args  Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "forcedeletemessages",
            description = "Deletion of Message Amount",
            alias = "fdm",
            arguments = {"Count"},
            permission = Globals.BOT_MANAGE,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean forcedeleteMessages(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            int count = 0;
            int total = 0;
            try {
                IChannel channel = event.getChannel();
                String origintopic = channel.getTopic();
                MessageHistory messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                if (messages.getLatestMessage().getLongID() == event.getMessage().getLongID()) {
                    while (!event.getMessage().isDeleted()) {
                        Thread.sleep(200);
                    }
                    messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                }
                total = messages.size();
                for (IMessage message : messages) {
                    if (!message.isDeleted()) {
                        BotUtils.deleteMessageOne(message);
                        count++;
                    }
                }
                Thread.sleep(1000);
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("del_wait_success")), true);
            } catch (Exception ex) {
                BotUtils.sendMessage(event.getChannel(), LANG.ERROR + String.format(LANG.getTranslation("deletion_error"), count, total, ex.getMessage()), true);
            }
        }).start();

        return true;
    }

    @Command(
            command = "shutdown",
            description = "Shutdown the bot",
            alias = "die",
            arguments = {},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean shutdownbot(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            try {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("shutdowninfo")), true);
                Thread.sleep(10000);
                System.exit(0);
            } catch (Exception ex) {
                Console.error("Error on shutdown: " + ex.getMessage());
            }
        }).start();
        return true;
    }

    @Command(
            command = "deleteprivmsg",
            description = "Deletes private Messages from Bot",
            alias = "dpm",
            arguments = {"Count"},
            permission = Globals.BOT_INFO,
            prefix = Globals.INFO_PREFIX
    )
    public boolean deleteprivateMessages(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            try {
                int count = 0;
                IPrivateChannel privateChannel = event.getAuthor().getOrCreatePMChannel();
                List<IMessage> messageList = privateChannel.getMessageHistory(Integer.parseInt(args[0])*3);
                for (IMessage message : messageList) {
                    if (message.getAuthor().isBot() && count <= Integer.parseInt(args[0])) {
                        BotUtils.deleteMessageOne(message);
                        count++;
                    }
                }
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("deleteprivinfo")), true);
            } catch (Exception ex) {
                Console.error(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage()));
            }
        }).start();
        return true;
    }

    @LanguageMethod(
            languagestringcount = 2
    )
    @Override
    public void setdefaultLanguage() {
        //Deletion
        DRIVER.setProperty(DEF_LANG, "del_topic", "Deletion %1s of %2s");
        DRIVER.setProperty(DEF_LANG, "del_wait_success", "Deletion invoked... Wait until messages disapear.");
    }
}
