package Discord;

import Events.EventListener;
import Main.MoMuOSBMain;
import Storage.ConfigDriver;
import Util.Console;
import sx.blah.discord.api.IDiscordClient;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class DiscordInit {

    private static DiscordInit instance;
    private IDiscordClient cli;
    public static DiscordInit getInstance() {
        if (instance == null) {
            instance = new DiscordInit();
        }
        return instance;
    }

    public void init() {
        String token = ConfigDriver.getInstance().getProperty("token", "");
        if (token.equals("")) {
            Console.error("Please provide a token inside of the config.properties");
            MoMuOSBMain.setRunstate(false);
        } else {
            Console.println("Bot is logging in. Please wait until its ready...");
             cli = BotUtils.getBuiltDiscordClient(token);
             cli.getDispatcher().registerListener(EventListener.getInstance());
             cli.login();
        }
    }
    public IDiscordClient getDiscordClient() {
        return cli;
    }
}
