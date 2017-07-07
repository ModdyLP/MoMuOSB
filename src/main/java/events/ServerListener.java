package events;

import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import discord.Stats;
import discord.SystemInfo;
import modules.RoleManagement;
import modules.music.MainMusic;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
public class ServerListener implements Fast{

    private static boolean running = false;

    private static ServerListener instance;

    public static ServerListener getInstance() {
        if (instance == null) {
            instance = new ServerListener();
        }
        return instance;
    }

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
            Console.println("Shard "+iShard.getInfo()[0]+": "+iShard.isReady()+" Servers: "+iShard.getGuilds().size()+"  Ping: "+iShard.getResponseTime());
        });
        Optional stream = INIT.BOT.getOurUser().getPresence().getStreamingUrl();
        Optional playtext = INIT.BOT.getOurUser().getPresence().getPlayingText();
        Console.println("Username: "+INIT.BOT.getOurUser().getName());
        Console.println("Status: "+INIT.BOT.getOurUser().getPresence().getStatus());
        if (stream.isPresent()) {
            Console.println("Streaming: "+stream.get());
        } else {
            Console.println("Streaming: OFFLINE");
        }
        if (playtext.isPresent()) {
            Console.println("Playing: "+playtext.get());
        } else {
            Console.println("Playing: NOTHING");
        }

        SystemInfo info = new SystemInfo();
        Console.println("SystemInfo: "+info.Info()+"\n");
        Command helpcommand = COMMAND.getCommandByName("help");
        Console.println("Type "+ DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString()+helpcommand.prefix()+helpcommand.command()+" for getting help.");
        Console.println("====================================Bot Start completed===============================");
        running = true;
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        Console.println("==================NEW SERVER |" + event.getGuild().getName() + "| " + event.getGuild().getStringID());
        if (SERVER_CONTROL.checkServerisBanned(event.getGuild())) {
            Console.println("Leave Banned Server: "+event.getGuild().getName());
            //event.getGuild().leave();
        } else {
            if (running) {
                Stats.addServer(event.getGuild());
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
    }

    private void saveGuilds() {
        Console.println("Server init");
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.MUSIC_MODULE);
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.JOIN_MODULE);
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.BAN_MODULE);

        for (IGuild server : INIT.BOT.getGuilds()) {
            saveGuild(server);
        }
        DRIVER.saveJson();
    }
    private void saveGuild(IGuild server) {
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
