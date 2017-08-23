package events;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord.BotUtils;
import discord.ServerControl;
import discord.Stats;
import discord.SystemInfo;
import modules.RoleManagement;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import util.*;
import modules.music.MainMusic;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.util.*;

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
    public void onMessageReceivedEvent(MessageReceivedEvent event) { // This method is NOT called because it doesn't have the @EventSubscriber annotation
        new Thread(() -> {
            try {
                Stats.addMessages();
                if (SERVER_CONTROL.checkServerisBanned(event.getGuild())) {
                    Console.println("Leave Banned Server: "+event.getGuild().getName());
                    BotUtils.sendPrivMessage(event.getGuild().getOwner().getOrCreatePMChannel(), "Your Server is on the banned Server List. Please contact webmaster@moddylp.de and describe why do you want to get unbanned.", false);
                    event.getGuild().leave();
                }
                //Check if Channel is Private (DM)
                //Console.debug("MI: "+event.getGuild().getStringID()+"   "+event.getChannel().getName()+"   "+event.getAuthor().getName());
                if (!event.getChannel().isPrivate()) {
                    String message = event.getMessage().getContent();
                    String[] messageparts = message.split(" ");
                    if (messageparts.length > 0) {
                        setArgsAndPrefix(messageparts, message);
                        Command command = COMMAND.getCommandByName(commandstring);
                        if (command != null && (botprefix + command.prefix()).equalsIgnoreCase(prefix)) {
                            Stats.addCommands();
                            Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                            //Check if Invoke Messages should be deleted
                            if (DRIVER.getProperty(DRIVER.CONFIG, "deleteinvokes", true).equals(true)) {
                                if (INIT.BOT.getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                                    Console.debug(Console.sendprefix + "Message deleted: [" + message + "]");
                                    event.getMessage().delete();
                                } else {
                                    Console.debug(Console.sendprefix + "Message not deleted: [" + message + "] -- nopermissions");
                                }
                            }
                            if (PERM.hasPermission(event.getAuthor(), event.getGuild(), command.permission())) {
                                initiateCommand(args, command, event);
                            } else {
                                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("nomanagepermission_error")), true);
                            }
                        }
                    }
                } else {
                    if (event.getMessage().getMentions().contains(INIT.BOT.getOurUser())) {
                        String[] messageparts = event.getMessage().getContent().trim().split(" ");
                        if (messageparts.length == 2) {
                            UserEvents.getInstance().setGenderRole(event, messageparts[1]);
                        } else {
                            BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.ERROR + LANG.getTranslation("invalid_count_gender"), true);
                        }
                    } else {
                        BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.ERROR + LANG.getTranslation("private_error"), true);
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

    /**
     * Executes the Command
     *
     * @param args    Arguments which should be delivered
     * @param command The Command Annotation Object
     * @param event   The Message Event
     */
    private void initiateCommand(String[] args, Command command, MessageReceivedEvent event) {
        try {
            ArrayList<String> newargs = new ArrayList<>();
            if (args.length >= command.arguments().length) {
                for (int i = 0; i < command.arguments().length; i++) {
                    if (command.arguments()[i].contains("[]")) {
                        newargs.addAll(Arrays.asList(args).subList(i, args.length));
                    } else {
                        newargs.add(args[i]);
                    }

                }
                if (newargs.size() < args.length) {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + String.format(LANG.getTranslation("tomanyarguments_error"), args.length, command.arguments().length)), true);
                } else {
                    String[] printargs = newargs.toArray(new String[]{});
                    COMMAND.getModules().get(command).invoke(COMMAND.getInstances().get(command), event, printargs);
                }
                Console.debug(Console.sendprefix+"New Args: "+newargs);
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
