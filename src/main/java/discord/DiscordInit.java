package discord;

import events.EventListener;
import main.Fast;
import storage.ConfigLoader;
import util.Console;
import sx.blah.discord.api.IDiscordClient;

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
             cli.login();
             BOT = cli;
        }
    }
    public IDiscordClient getDiscordClient() {
        return cli;
    }
}
