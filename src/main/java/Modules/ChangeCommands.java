package Modules;

import Discord.BotUtils;
import Discord.DiscordInit;
import Events.Command;
import Events.Module;
import Util.Console;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.Image;
import sx.blah.discord.util.PermissionUtils;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class ChangeCommands extends Module {

    private static Module instance;

    @Command(
            command = "setavatar",
            description = "Change the Bot Avatar",
            alias = "sa",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean changeAvatar(MessageReceivedEvent event, String[] args) {
        if (args.length > 0) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changeAvatar(Image.forUrl("png", args[0]));
        } else {
            Console.error("No enought arguments");
            BotUtils.sendMessage(event.getChannel(), "Not enought arguments: Missing Url");
        }
        return true;
    }

    @Command(
            command = "setplaying",
            description = "Change the Bot Game",
            alias = "sp",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setplaying(MessageReceivedEvent event, String[] args) {
        if (args.length > 0) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changePlayingText(args[0]);
        } else {
            Console.error("No enought arguments");
            BotUtils.sendMessage(event.getChannel(), "Not enought arguments: Missing Game");
        }
        return true;
    }
    @Command(
            command = "setusername",
            description = "Change the Bot Name",
            alias = "sun",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setUsername(MessageReceivedEvent event, String[] args) {
        if (args.length > 0) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changeUsername(args[0]);
        } else {
            Console.error("No enought arguments");
            BotUtils.sendMessage(event.getChannel(), "Not enought arguments: Missing Name");
        }
        return true;
    }
}
