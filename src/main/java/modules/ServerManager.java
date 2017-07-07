package modules;

import discord.BotUtils;
import events.Command;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import util.Fast;
import util.Globals;
import util.SMB;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
public class ServerManager implements Fast{
    @Command(
            command = "setserverenabled",
            alias = "sse",
            description = "Removes a Server from List",
            arguments = {"ServerID", "Module"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean removeDisabledServer(MessageReceivedEvent event, String[] args) {
        try {
            SERVER_CONTROL.removeDisabledServer(INIT.BOT.getGuildByID(Long.valueOf(args[0])), SERVER_CONTROL.MUSIC_MODULE);
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
            ex.printStackTrace();
        }
        return true;
    }
}
