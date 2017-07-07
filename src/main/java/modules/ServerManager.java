package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import storage.LanguageInterface;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.util.EmbedBuilder;
import util.Fast;
import util.Globals;
import util.Markdown;
import util.SMB;

import java.awt.*;
import java.util.List;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
public class ServerManager extends Module implements Fast{
    @Command(
            command = "setserverenabled",
            alias = "sse",
            description = "Removes a Server from List",
            arguments = {"ServerID", "Module {join, music, banned}"},
            permission = Globals.BOT_MANAGE,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean removeDisabledServer(MessageReceivedEvent event, String[] args) {
        try {
            SERVER_CONTROL.removeDisabledServer(INIT.BOT.getGuildByID(Long.valueOf(args[0])), args[1]);
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
            ex.printStackTrace();
        }
        return true;
    }

    @Command(
            command = "banserver",
            alias = "bans",
            description = "Ban a Server",
            arguments = {"ServerID"},
            permission = Globals.BOT_MANAGE,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean banServer(MessageReceivedEvent event, String[] args) {
        try {
            SERVER_CONTROL.addBannedServer(args[0]);
            DRIVER.saveJson();
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
            ex.printStackTrace();
        }
        return true;
    }
    @Command(
            command = "getserver",
            arguments = {},
            description = "Get Servers",
            alias = "gets",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean getServer(MessageReceivedEvent event, String[] args) {
        try {
            List<IGuild> server = INIT.BOT.getGuilds();
            EmbedBuilder builder = new EmbedBuilder();
            builder.withTitle("ServerList");
            builder.withDescription(Markdown.bold("Size: "+server.size())+" \n");
            for (IGuild serverinst: server) {
                builder.appendDesc(Markdown.bold(serverinst.getName())+": "+serverinst.getStringID()+" \n");
            }
            builder.withColor(Color.green);
            BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder, false);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.SUCCESS+LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
        }
        return true;
    }

    @LanguageMethod(
            languagestringcount = 0
    )
    @Override
    public void setdefaultLanguage() {
        super.setdefaultLanguage();
    }
}
