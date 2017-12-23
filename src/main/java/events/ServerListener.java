package events;

import discord.BotUtils;
import discord.Stats;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.ReadyEvent;
import sx.blah.discord.handle.impl.events.guild.GuildCreateEvent;
import sx.blah.discord.handle.impl.events.shard.DisconnectedEvent;
import sx.blah.discord.handle.impl.events.shard.ReconnectFailureEvent;
import sx.blah.discord.handle.impl.events.shard.ReconnectSuccessEvent;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
public class ServerListener implements Fast {

    public static boolean running = false;

    private static ServerListener instance;

    public static ServerListener getInstance() {
        if (instance == null) {
            instance = new ServerListener();
        }
        return instance;
    }

    @EventSubscriber
    public void onReadyEvent(ReadyEvent event) { // This method is called when the ReadyEvent is dispatched
        BotUtils.initBot();
    }

    @EventSubscriber
    public void onGuildCreate(GuildCreateEvent event) {
        Console.println("==================NEW SERVER |" + event.getGuild().getName() + "| " + event.getGuild().getStringID());
        if (SERVER_CONTROL.checkServerisBanned(event.getGuild())) {
            Console.println("Leave Banned Server: " + event.getGuild().getName());
            BotUtils.sendPrivMessage(event.getGuild().getOwner().getOrCreatePMChannel(), "Your Server is on the banned Server List. Please contact webmaster@moddylp.de and describe why do you want to get unbanned.", false);
            event.getGuild().leave();
        } else {
            Stats.addServer(event.getGuild());
            Console.debug("Adding Permission for new Server");
            List<IGuild> server = new ArrayList<>();
            server.add(event.getGuild());
            PERM.setDefaultPermissions(server, true);
            saveGuild(event.getGuild());
            DRIVER.saveJson();
            Console.debug("===================new Server added====");
        }
    }

    @EventSubscriber
    public void onDiscconect(DisconnectedEvent event) {
        Console.error("=====================================================================================\n" +
                "ERROR DISCONNECTED: " + event.getReason() + "\n" +
                "========================================================================================");
    }

    @EventSubscriber
    public void onReconnectFail(ReconnectFailureEvent event) {
        Console.error("=====================================================================================\n" +
                "ERROR Reconnect Failed: " + event.getCurrentAttempt() + "\n" +
                "========================================================================================");
    }

    @EventSubscriber
    public void onReconnectSuccess(ReconnectSuccessEvent event) {
        BotUtils.initBot();
    }


    public void saveGuilds() {
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.MUSIC_MODULE);
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.JOIN_MODULE);
        SERVER_CONTROL.loadSavedServer(SERVER_CONTROL.BAN_MODULE);

        for (IGuild server : INIT.BOT.getGuilds()) {
            saveGuild(server);
        }
        DRIVER.saveJson();
    }

    private void saveGuild(IGuild server) {
        if (DRIVER.getProperty(DRIVER.CONFIG, SERVER_CONTROL.MUSIC_MODULE + "_enabled_default", false).equals(true)) {
            if (DRIVER.hasKey(DRIVER.MODULE, SERVER_CONTROL.MUSIC_MODULE + "_enabled_servers")) {
                SERVER_CONTROL.addEnabledServer(server, SERVER_CONTROL.MUSIC_MODULE);
            }
        }
        if (DRIVER.getProperty(DRIVER.CONFIG, SERVER_CONTROL.JOIN_MODULE + "_enabled_default", false).equals(true)) {

            if (DRIVER.hasKey(DRIVER.MODULE, SERVER_CONTROL.JOIN_MODULE + "_enabled_servers")) {
                SERVER_CONTROL.addEnabledServer(server, SERVER_CONTROL.JOIN_MODULE);
            }
        }
    }
}
