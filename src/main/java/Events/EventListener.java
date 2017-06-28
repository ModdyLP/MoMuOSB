package Events;

import Discord.BotUtils;
import Discord.DiscordInit;
import Storage.ConfigDriver;
import Util.Console;
import Util.GetAnnotation;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Set;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class EventListener {
    private HashMap<Command, Method> modules = new HashMap<>();
    private HashMap<Command, Module> instances = new HashMap<>();
    private static EventListener instance;
    public static EventListener getInstance() {
        if (instance == null) {
            instance = new EventListener();
        }
        return instance;
    }
    @EventSubscriber
    public void onMessageReceivedEvent(MessageReceivedEvent event) { // This method is NOT called because it doesn't have the @EventSubscriber annotation
        try {
            if (!event.getChannel().isPrivate()) {
                String message = event.getMessage().getContent();
                Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                for (Command command : modules.keySet()) {
                    String[] args = new String[] {};
                    if (message.startsWith(BotUtils.BOT_PREFIX + command.command().toLowerCase())) {
                        if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(command.permission())) {
                            if (!message.endsWith(BotUtils.BOT_PREFIX + command.command().toLowerCase()) && !message.endsWith(" ")) {
                                args = message.substring((BotUtils.BOT_PREFIX + command.command()).length() + 1).split(" ");
                            }
                            modules.get(command).invoke(instances.get(command), event, args);
                        } else {
                            BotUtils.sendMessage(event.getChannel(), "You have no Permission to use this command.");
                        }

                    } else if (message.startsWith(BotUtils.BOT_PREFIX + command.alias().toLowerCase()) && !message.endsWith(" ")) {
                        if (event.getAuthor().getPermissionsForGuild(event.getGuild()).contains(command.permission())) {
                            if (!message.endsWith(BotUtils.BOT_PREFIX + command.alias().toLowerCase())) {
                                args = message.substring((BotUtils.BOT_PREFIX + command.alias()).length() + 1).split(" ");
                            }
                            modules.get(command).invoke(instances.get(command), event, args);
                        } else {
                            BotUtils.sendMessage(event.getChannel(), "You have no Permission to use this command.");
                        }
                    }
                }
                if (ConfigDriver.getInstance().getProperty("deleteinvokes", "true").equals("true")) {
                    if (DiscordInit.getInstance().getDiscordClient().getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                        Console.debug(Console.sendprefix + "Message deleted");
                        event.getMessage().delete();
                    } else {
                        BotUtils.sendMessage(event.getChannel(), "The Bot has no Permission to Manage Permissions");
                    }
                }
            }
        }catch (Exception ex) {
            Console.error("Error on execution: "+ex.getMessage());
            ex.printStackTrace();
        }
    }
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        Console.println("Bot is ready");
        Console.println("Shards: "+DiscordInit.getInstance().getDiscordClient().getShardCount());
        StringBuilder serverstr = new StringBuilder();
        for (IGuild server: DiscordInit.getInstance().getDiscordClient().getGuilds()) {
            serverstr.append("\n["+server.getName()).append("   ").append(server.getStringID()+"]");
        }
        Console.println("Servers: "+serverstr);
    }
    public void registerCommand(Class module, Module instance) {
        HashMap<Command, Method> annotations = GetAnnotation.getAnnotation(module);
        for (Command anno : annotations.keySet()) {
            modules.put(anno, annotations.get(anno));
            instances.put(anno, instance);
        }

    }
    public Set<Command> getAllCommands() {
        return modules.keySet();
    }
    public HashMap<Command, Method> getAllModules() {
        return modules;
    }
}
