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

    @Command(
            command = "setavatar",
            description = "Change the Bot Avatar",
            alias = "sa",
            arguments = {"ImageLink"},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean changeAvatar(MessageReceivedEvent event, String[] args) {
            DiscordInit.getInstance().getDiscordClient().changeAvatar(Image.forUrl("png", args[0]));
        return true;
    }

    @Command(
            command = "setstream",
            description = "Change the Bot Avatar",
            alias = "sst",
            arguments = {"Game", "StreamUrl"},
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setStream(MessageReceivedEvent event, String[] args) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().streaming(args[0], args[1]);
        return true;
    }

    @Command(
            command = "setplaying",
            arguments = {"Game"},
            description = "Change the Bot Game",
            alias = "sp",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setplaying(MessageReceivedEvent event, String[] args) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changePlayingText(args[0]);
        return true;
    }
    @Command(
            command = "setusername",
            arguments = {"Name"},
            description = "Change the Bot Name",
            alias = "sun",
            permission = Permissions.ADMINISTRATOR
    )
    public boolean setUsername(MessageReceivedEvent event, String[] args) {
            Console.debug(args[0]);
            DiscordInit.getInstance().getDiscordClient().changeUsername(args[0]);
        return true;
    }
}
