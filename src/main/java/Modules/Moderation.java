package Modules;

import Discord.BotUtils;
import Events.Command;
import Events.Module;
import Util.Console;
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


    @Command(
            command = "deletemessages",
            description = "Deletion of Message Amount",
            alias = "dm",
            permission = Permissions.MANAGE_MESSAGES
    )
    public boolean deleteMessages(MessageReceivedEvent event, String[] args) {
        new Thread(new Runnable() {
            public void run(){
                int total = 0;
                try {
                    IChannel channel = event.getChannel();
                    String origintopic = channel.getTopic();
                    if (args.length > 0) {
                        MessageHistory messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                        total = messages.size();
                        BotUtils.bulkdeleteMessage(channel, messages);
                        channel.changeTopic(origintopic + " [Deletion: "+total+"]");
                        Console.debug(Console.sendprefix+"FDM: "+total);
                        Thread.sleep(2000);
                        channel.changeTopic(origintopic);
                    }

                } catch (Exception ex) {
                    BotUtils.sendMessage(event.getChannel(), "Deletion of Messages failed ("+total+"): "+ex.getMessage(), true);
                }
            }
        }).start();

        return true;
    }
    @Command(
            command = "forcedeletemessages",
            description = "Deletion of Message Amount",
            alias = "fdm",
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
                    if (args.length > 0) {
                        MessageHistory messages = channel.getMessageHistory(Integer.parseInt(args[0]));
                        total = messages.size();
                        for (IMessage message: messages) {
                            if (!message.isDeleted()) {
                                BotUtils.deleteMessageOne(message);
                                count++;
                                channel.changeTopic(origintopic + " [Deletion: "+count+" of "+total+"]");
                            }
                        }
                        Thread.sleep(1000);
                        channel.changeTopic(origintopic);
                        Console.debug(Console.sendprefix+"FDM: "+count+" of "+total);
                    }
                } catch (Exception ex) {
                    BotUtils.sendMessage(event.getChannel(), "Force Deletion of Messages failed ("+count+" of "+total+"): "+ex.getMessage(), true);
                }
            }
        }).start();

        return true;
    }
}
