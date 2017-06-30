package events;

import discord.BotUtils;
import main.Fast;
import util.Console;
import util.GetAnnotation;
import org.tritonus.share.ArraySet;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class EventListener implements Fast{
    private HashMap<Command, Method> modules = new HashMap<>();
    private HashMap<Command, Module> instances = new HashMap<>();
    private static EventListener instance;

    /**
     * Get Instance
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
     * @param event The Event
     */
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) { // This method is NOT called because it doesn't have the @EventSubscriber annotation
        new Thread(new Runnable() {
            public void run() {
                try {
                    //Check if Channel is Private (DM)
                    if (!event.getChannel().isPrivate()) {
                        String message = event.getMessage().getContent();
                        //Iterate commands
                        for (Command command : modules.keySet()) {
                            String[] args = new String[]{};
                            //Check if Message contains Prefix
                            if (message.startsWith(command.prefix() + command.command().toLowerCase()) || message.startsWith(command.prefix() + command.alias().toLowerCase())) {
                                Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                                //Check if Invoke Messages should be deleted
                                if (DRIVER.getProperty(DRIVER.CONFIG,"deleteinvokes", true).equals(true)) {
                                    if (INIT.BOT.getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                                        Console.debug(Console.sendprefix + "Message deleted: [" + event.getMessage().getContent() + "]");
                                        event.getMessage().delete();
                                    } else {
                                        BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("nomanagepermission_error"), true);
                                    }
                                }
                            }
                            //Check each command if the command was called
                            if (message.startsWith(command.prefix() + command.command().toLowerCase())) {
                                //Check Permissions
                                if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(command.permission())) {
                                    if (!message.endsWith(command.prefix() + command.command().toLowerCase()) && !message.endsWith(" ")) {
                                        args = message.substring((command.prefix() + command.command()).length() + 1).split(" ");
                                    }
                                    //Execute Command
                                    initiateCommand(args, command, event);
                                } else {
                                    BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("nopermissions_error"), true);
                                }
                            //Check each alias if the alias was called
                            } else if (message.startsWith(command.prefix() + command.alias().toLowerCase()) && !message.endsWith(" ")) {
                                //Check Permissions
                                if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(command.permission())) {
                                    if (!message.endsWith(command.prefix() + command.alias().toLowerCase()) && !message.endsWith(" ")) {
                                        args = message.substring((command.prefix() + command.alias()).length() + 1).split(" ");
                                    }
                                    //Execute Command
                                    initiateCommand(args, command, event);
                                } else {
                                    BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("nopermissions_error"), true);
                                }
                            }
                        }

                    } else {
                        BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.ERROR+LANG.getTranslation("private_error"));
                    }
                } catch (Exception ex) {
                    Console.error(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace())));
                    ex.printStackTrace();
                }
            }
        }).start();
    }

    /**
     * Executes the Command
     * @param args Arguments which should be delivered
     * @param command The Command Annotation Object
     * @param event The Message Event
     */
    private void initiateCommand(String[] args, Command command, MessageReceivedEvent event) {
        try {
            if (args.length == command.arguments().length) {
                modules.get(command).invoke(instances.get(command), event, args);
            } else {
                if (args.length < command.arguments().length) {
                    BotUtils.sendMessage(event.getChannel(), String.format(LANG.getTranslation("tofewarguments_error"),args.length,command.arguments().length), true);
                } else {
                    BotUtils.sendMessage(event.getChannel(), String.format(LANG.getTranslation("tomanyarguments_error"),args.length,command.arguments().length), true);
                }
            }
        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("execution_error"), Arrays.toString(ex.getStackTrace())));
        }
    }

    /**
     * If Bot is started and ready to recieve anything
     * @param event The Event
     */
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        Console.println("Bot is ready");
        Console.println("Shards: "+INIT.BOT.getShardCount());
        StringBuilder serverstr = new StringBuilder();
        int count = 1;
        for (IGuild server: INIT.BOT.getGuilds()) {
            serverstr.append("\n")
                    .append(count)
                    .append(". [")
                    .append(server.getName())
                    .append("   ")
                    .append(server.getStringID())
                    .append("]");
            count++;
        }
        Console.println("Servers: "+serverstr);
        RegisterCommands.registerAll();
        INIT.BOT.changePlayingText(DRIVER.getProperty(DRIVER.CONFIG,"defaultplaying", "TestBetrieb").toString());
        INIT.BOT.changeUsername(DRIVER.getProperty(DRIVER.CONFIG,"defaultUsername", "MoMuOSB").toString());
        Console.println("Bot Start completed");
    }
    void registerCommand(Class module, Module instance) {
        HashMap<Command, Method> annotations = GetAnnotation.getAnnotation(module);
        for (Command anno : annotations.keySet()) {
            if (!getAllCommandsAsString().contains(anno.command())) {
                if (!getAllAliasAsString().contains(anno.alias())) {
                    modules.put(anno, annotations.get(anno));
                    instances.put(anno, instance);
                } else {
                    Console.error("Duplicate Alias: "+anno.alias());
                }
            } else {
                Console.error("Duplicate Command: "+anno.command());
            }
        }

    }
    public Set<Command> getAllCommands() {
        return modules.keySet();
    }
    public Set<String> getAllCommandsAsString() {
        Set<String> commandsAsString = new ArraySet<>();
        for (Command element: getAllCommands()) {
            commandsAsString.add(element.command());
        }
        return commandsAsString;
    }
    public Set<String> getAllAliasAsString() {
        Set<String> commandsAsString = new ArraySet<>();
        for (Command element: getAllCommands()) {
            commandsAsString.add(element.alias());
        }
        return commandsAsString;
    }
    public HashMap<Command, Method> getAllModules() {
        return modules;
    }
}
