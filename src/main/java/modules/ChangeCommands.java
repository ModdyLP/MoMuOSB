package modules;

import discord.BotUtils;
import discord.DiscordInit;
import events.Command;
import events.Module;
import util.Console;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.Image;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class ChangeCommands extends Module {

    /**
     * Change the Avatar of the Bot
     * @param event MessageEvent
     * @param args Argumente
     * @return state
     */
    @Command(
            command = "setavatar",
            description = "Change the Bot Avatar",
            alias = "sa",
            arguments = {"ImageLink"},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean changeAvatar(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            DiscordInit.getInstance().getDiscordClient().changeAvatar(Image.forUrl("png", args[0]));
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("botowner_error"), true);
        }
        return true;
    }

    /**
     * Change the Stream of the Bot
     * @param event MessageEvent
     * @param args Argumente
     * @return state
     */
    @Command(
            command = "setstream",
            description = "Change the Bot Stream",
            alias = "sst",
            arguments = {"Game", "StreamUrl"},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setStream(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().streaming(args[0], args[1]);
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("botowner_error"), true);
        }
        return true;
    }

    /**
     * Change the Game of the Bot
     * @param event MessageEvent
     * @param args Argumente
     * @return state
     */
    @Command(
            command = "setplaying",
            arguments = {"Game"},
            description = "Change the Bot Game",
            alias = "sp",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setplaying(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changePlayingText(args[0]);
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("botowner_error"), true);
        }
        return true;
    }
    /**
     * Change the Username of the Bot
     * @param event MessageEvent
     * @param args Argumente
     * @return state
     */
    @Command(
            command = "setusername",
            arguments = {"Name"},
            description = "Change the Bot Name",
            alias = "sun",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setUsername(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(DiscordInit.getInstance().getDiscordClient().getApplicationOwner())) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changeUsername(args[0]);
        } else {
            BotUtils.sendMessage(event.getChannel(), LANG.ERROR+LANG.getTranslation("botowner_error"), true);
        }
        return true;
    }
}
