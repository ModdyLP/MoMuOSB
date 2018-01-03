package events;

import com.vdurmont.emoji.EmojiManager;
import discord.BotUtils;
import discord.Stats;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.*;
import util.Console;
import util.Fast;
import util.SMB;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class EventListener implements Fast {
    private static EventListener instance;
    private String botprefix;
    private String[] args;
    private String commandstring;
    private String prefix;


    /**
     * Get Instance
     *
     * @return Class Instance
     */
    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }

    /**
     * If Bot recieves any Message
     *
     * @param event The Event
     */
    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) {
        IMessage message = event.getMessage();
        IReaction reaction = message.getReactionByUnicode(EmojiManager.getForAlias("x"));
        if (reaction != null && reaction.getUserReacted(INIT.BOT.getOurUser()) && reaction.getUsers().size() > 1) {
            BotUtils.deleteMessageOne(message);
        }
    }
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) { // This method is NOT called because it doesn't have the @EventSubscriber annotation
        new Thread(() -> {
            try {
                Stats.addMessages();
                if (SERVER_CONTROL.checkServerisBanned(event.getGuild())) {
                    Console.println("Leave Banned Server: " + event.getGuild().getName());
                    BotUtils.sendPrivMessage(event.getGuild().getOwner().getOrCreatePMChannel(), "Your Server is on the banned Server List. Please contact webmaster@moddylp.de and describe why do you want to get unbanned.", false);
                    event.getGuild().leave();
                    return;
                }
                //Check if Channel is Private (DM)
                //Console.debug("MI: "+event.getGuild().getStringID()+"   "+event.getChannel().getName()+"   "+event.getAuthor().getName());
                if (!event.getChannel().isPrivate()) {
                    String message = event.getMessage().getContent();
                    message = message.replace("  ", " ");
                    String[] messageparts = message.split(" ");
                    if (messageparts.length > 0) {
                        setArgsAndPrefix(messageparts, message);
                        Command command = COMMAND.getCommandByName(commandstring);
                        if (command != null && (botprefix + command.prefix()).equalsIgnoreCase(prefix)) {
                            Stats.addCommands();
                            Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                            //Check if Invoke Messages should be deleted
                            deleteInvoke(event, message);
                            if (PERM.hasPermission(event.getAuthor(), event.getGuild(), command.permission())) {
                                initiateCommand(args, command, event);
                            } else {
                                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("nomanagepermission_error")), true);
                            }
                        } else if (command != null && prefix.contains(botprefix)) {
                            deleteInvoke(event, message);
                            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + String.format(LANG.getTranslation("prefixinccorect"), command.prefix())), true);
                        }
                    }
                } else {
                    if (event.getMessage().getMentions().contains(INIT.BOT.getOurUser()) && (event.getMessage().getContent().contains("f") || event.getMessage().getContent().contains("m"))) {
                        String[] messageparts = event.getMessage().getContent().trim().split(" ");
                        if (messageparts.length == 2) {
                            UserEvents.getInstance().setGenderRole(event, messageparts[1]);
                        }
                    } else if (event.getMessage().getContent().contains("~") && event.getMessage().getContent().contains(" ") && event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
                        String msgcontent = event.getMessage().getContent();
                        String userid = msgcontent.substring(msgcontent.indexOf("~") + 1, msgcontent.indexOf(" ")).trim();
                        Console.debug("Search for User: |"+userid+"|");
                        IUser user = Utils.getUserByID(userid);
                        if (user != null) {
                            Console.debug("Mention: " + user.getName());
                            IMessage message = BotUtils.sendPrivMessage(user.getOrCreatePMChannel(), event.getMessage().getContent().replace("~" + userid, ""), false);
                            if (message != null) {
                                BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "Message was delivered to " + user.mention()+"  "+userid, true);
                            } else {
                                BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "Message was NOT delivered to " + user.mention()+"  "+userid, true);
                            }
                        } else {
                            BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), "Message was NOT delivered to " + userid, true);
                        }
                    } else if (event.getMessage().getContent().contains("~") && event.getMessage().getContent().contains(" ") && !event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
                        BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.getTranslation("private_msg_not_owner"), true);
                    } else {
                        BotUtils.sendPrivMessage(INIT.BOT.getApplicationOwner().getOrCreatePMChannel(), event.getAuthor().mention() + "("+event.getAuthor().getName()+")"+ ": " + event.getMessage().getContent(), false);
                    }
                }
            } catch (Exception ex) {
                BotUtils.sendMessage(event.getChannel(), String.format(LANG.getTranslation("commonmessage_error"), "Execution failed"), true);
                Console.error(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace())));
                Console.error(ex);
            }
        }).start();
    }

    public void setArgsAndPrefix(String[] messageparts, String message) {
        botprefix = DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString();
        commandstring = messageparts[0].replaceFirst(botprefix, "");
        Iterator<String> iterator = COMMAND.getAllPrefixe().iterator();
        while (iterator.hasNext()) {
            commandstring = commandstring.replace(iterator.next(), "");
        }
        prefix = messageparts[0].replace(commandstring, "");
        args = new String[]{};
        if (messageparts.length > 1) {
            args = message.replace(messageparts[0], "").trim().split(" ");
        }
    }
    private void deleteInvoke(MessageReceivedEvent event, String message){
        if (DRIVER.getProperty(DRIVER.CONFIG, "deleteinvokes", true).equals(true)) {
            if (INIT.BOT.getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                Console.debug(Console.sendprefix + "Message deleted: [" + message + "]");
                event.getMessage().delete();
            } else {
                Console.debug(Console.sendprefix + "Message not deleted: [" + message + "] -- nopermissions");
            }
        }
    }

    /**
     * Executes the Command
     *
     * @param args    Arguments which should be delivered
     * @param command The Command Annotation Object
     * @param event   The Message Event
     */
    private void initiateCommand(String[] args, Command command, MessageReceivedEvent event) {
        Console.debug(Console.sendprefix + "ORIGINArgs: " + Arrays.toString(args));
        try {
            ArrayList<String> newargs = new ArrayList<>();
            if (args.length >= command.arguments().length) {
                for (String arg: command.arguments()) {
                    if (arg.contains("[]")) {
                        newargs.addAll(Arrays.asList(args));
                    }
                }
                if (args.length > command.arguments().length && newargs.size() == 0) {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + String.format(LANG.getTranslation("tomanyarguments_error"), args.length, command.arguments().length)), true);
                } else {
                    if (newargs.size() > 0) {
                        String[] printargs = newargs.toArray(new String[]{});
                        Console.debug(Console.sendprefix + "NEWArgs: " + Arrays.toString(printargs));
                        COMMAND.getModules().get(command).invoke(COMMAND.getInstances().get(command), event, printargs);
                    } else {
                        Console.debug(Console.sendprefix + "NORMArgs: " + Arrays.toString(args));
                        COMMAND.getModules().get(command).invoke(COMMAND.getInstances().get(command), event, args);
                    }
                }
                Console.debug(Console.sendprefix + "Size: " +args.length+"  "+newargs.size());
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + String.format(LANG.getTranslation("tofewarguments_error"), args.length, command.arguments().length)), true);
            }

        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("execution_error"), ex.getCause()));
            Console.error(ex);
        }
    }

    /**
     * If Bot is started and ready to recieve anything
     *
     * @param event The Event
     */

}
