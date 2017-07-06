package events;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord.BotUtils;
import discord.ServerControl;
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
    private static boolean running = false;
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
                //Check if Channel is Private (DM)
                if (!event.getChannel().isPrivate()) {
                    String message = event.getMessage().getContent();
                    String[] messageparts = message.split(" ");
                    if (messageparts.length > 0) {

                        Command command = COMMAND.getCommandByName(commandstring);
                        if (command != null && (botprefix + command.prefix()).equalsIgnoreCase(prefix)) {
                            Console.debug(Console.recievedprefix + "Message: " + message + " Author: " + event.getAuthor().getName() + " Channel: " + event.getChannel().getName());
                            //Check if Invoke Messages should be deleted
                            if (DRIVER.getProperty(DRIVER.CONFIG, "deleteinvokes", true).equals(true)) {
                                if (INIT.BOT.getOurUser().getPermissionsForGuild(event.getGuild()).contains(Permissions.MANAGE_MESSAGES)) {
                                    Console.debug(Console.sendprefix + "Message deleted: [" + message + "]");
                                    event.getMessage().delete();
                                } else {
                                    Console.debug(Console.sendprefix + "Message not deleted: [" + message + "] -- nopermissions");
                                    BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("nomanagepermission_error")));
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
                        UserEvents.getInstance().setGenderRole(event, args[0]);
                    } else {
                        BotUtils.sendPrivMessage(event.getAuthor().getOrCreatePMChannel(), LANG.ERROR + LANG.getTranslation("private_error"));
                    }
                }
            } catch (Exception ex) {
                Console.error(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace())));
                ex.printStackTrace();
            }
        }).start();
    }

    public void setArgsAndPrefix(String[] messageparts, String message) {
        botprefix = DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString();
        commandstring = messageparts[0].replace(botprefix, "");
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
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + String.format(LANG.getTranslation("tofewarguments_error"), args.length, command.arguments().length)), true);
            }

        } catch (Exception ex) {
            Console.error(String.format(LANG.getTranslation("execution_error"), ex.getCause()));
            ex.printStackTrace();
        }
    }

    /**
     * If Bot is started and ready to recieve anything
     *
     * @param event The Event
     */
    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
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

        saveGuilds();
        Console.println("====================================Bot Status========================================");
        INIT.BOT.getShards().forEach(iShard -> {
            Console.println("Shard "+iShard.getInfo()[0]+": "+iShard.isReady());
        });
        Command helpcommand = COMMAND.getCommandByName("help");
        Console.println("Type "+ DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString()+helpcommand.prefix()+helpcommand.command()+" for getting help.");
        Console.println("====================================Bot Start completed===============================");
        running = true;
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        Console.println("==================NEW SERVER |" + event.getGuild().getName() + "| " + event.getGuild().getStringID());
        if (running) {
            Console.debug("Adding Permission for new Server");
            List<IGuild> server = new ArrayList<>();
            server.add(event.getGuild());
            PERM.setDefaultPermissions(server, true);
            Console.debug("Adding Disabled Server for new Server");
            saveGuild(event.getGuild());
            DRIVER.saveJson();
        }
        Console.debug("===================new Server added====");
    }

    public void saveGuilds() {
        for (IGuild server : INIT.BOT.getGuilds()) {
            saveGuild(server);
        }
        DRIVER.saveJson();
    }
    public void saveGuild(IGuild server) {
        if (DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.MUSIC_MODULE + "_disabled_default") || DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.JOIN_MODULE + "_disabled_default")) {
            if (DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.MUSIC_MODULE + "_disabled_servers")) {
                SERVER_CONTROL.addDisabledServer(server, running, SERVER_CONTROL.MUSIC_MODULE);
            }
            if (DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.JOIN_MODULE + "_disabled_servers")) {
                SERVER_CONTROL.addDisabledServer(server, running, SERVER_CONTROL.JOIN_MODULE);
            }
            if (!DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.MUSIC_MODULE + "_disabled_servers")) {
                SERVER_CONTROL.addDisabledServer(server, true, SERVER_CONTROL.MUSIC_MODULE);
            }
            if (!DRIVER.hasKey(DRIVER.CONFIG, SERVER_CONTROL.JOIN_MODULE + "_disabled_servers")) {
                SERVER_CONTROL.addDisabledServer(server, true, SERVER_CONTROL.JOIN_MODULE);
            }
        }
    }
}
