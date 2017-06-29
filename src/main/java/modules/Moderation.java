package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import util.Console;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.MessageHistory;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class Moderation extends Module{

    /**
     * Deletes a List Message
     * @param event MessageEvent
     * @param args Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "deletemessages",
            description = "Deletion of Message Amount",
            alias = "dm",
            arguments = {"Count"},
            permission = Permissions.MANAGE_MESSAGES
    )
    public boolean deleteMessages(MessageReceivedEvent event, String[] args) {
        new Thread(new Runnable() {
            public void run(){
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
                        BotUtils.bulkdeleteMessage(channel, messages);
                        channel.changeTopic(origintopic + " ["+String.format(LANG.getTranslation("del_topic"), total, total)+"]");
                        Console.debug(Console.sendprefix+"DM: "+total);
                        Thread.sleep(2000);
                        channel.changeTopic(origintopic);

                } catch (Exception ex) {
                    BotUtils.sendMessage(event.getChannel(), LANG.ERROR+String.format(LANG.getTranslation("deletion_error"), total, total, ex.getMessage()), true);
                }
            }
        }).start();

        return true;
    }
    /**
     * Force Deletes a List Message
     * @param event MessageEvent
     * @param args Argumente [Not needed]
     * @return state
     */
    @Command(
            command = "forcedeletemessages",
            description = "Deletion of Message Amount",
            alias = "fdm",
            arguments = {"Count"},
            permission = Permissions.MANAGE_MESSAGES
    )
    public boolean forcedeleteMessages(MessageReceivedEvent event, String[] args) {
        new Thread(new Runnable() {
            public void run(){
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
                        for (IMessage message: messages) {
                            if (!message.isDeleted()) {
                                BotUtils.deleteMessageOne(message);
                                count++;
                                channel.changeTopic(origintopic + " ["+String.format(LANG.getTranslation("del_topic"), count, total)+"]");
                            }
                        }
                        Thread.sleep(1000);
                        channel.changeTopic(origintopic);
                        Console.debug(Console.sendprefix+"FDM: "+count+" of "+total);
                } catch (Exception ex) {
                    BotUtils.sendMessage(event.getChannel(), LANG.ERROR+String.format(LANG.getTranslation("deletion_error"), count, total, ex.getMessage()), true);
                }
            }
        }).start();

        return true;
    }
}
