package discord;

import events.EventListener;
import events.ServerListener;
import events.UserEvents;
import util.Fast;
import sx.blah.discord.api.IDiscordClient;
import util.Console;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class DiscordInit implements Fast{

    private static DiscordInit instance;
    private IDiscordClient cli;
    public IDiscordClient BOT;
    /**
     * Get Instance
     * @return Class Instance
     */
    public static DiscordInit getInstance() {
        if (instance == null) {
            instance = new DiscordInit();
        }
        return instance;
    }

    /**
     * Init the Bot
     */
    public void init() {
        String token = DRIVER.getPropertyOnly(DRIVER.CONFIG,"token").toString();
        if (token.equals("")) {
            Console.error(LANG.getTranslation("token_error"));

        } else {
            Console.println(LANG.getTranslation("login_info"));
             cli = BotUtils.getBuiltDiscordClient(token);
             cli.getDispatcher().registerListener(EventListener.getInstance());
             cli.getDispatcher().registerListener(ServerListener.getInstance());
             cli.getDispatcher().registerListener(UserEvents.getInstance());
             cli.login();
             BOT = cli;
        }
    }
    public IDiscordClient getDiscordClient() {
        return cli;
    }
}
