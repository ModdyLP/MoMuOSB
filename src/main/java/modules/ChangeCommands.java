package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import main.Prefix;
import org.apache.commons.lang3.math.NumberUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.Image;
import util.SMB;
import util.Utils;

import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;

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
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean changeAvatar(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            if (args[0].equalsIgnoreCase("NA")) {
                INIT.BOT.changeAvatar(Image.defaultAvatar());

            } else {
                INIT.BOT.changeAvatar(Image.forUrl("png", args[0]));
            }
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
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
            arguments = {"StreamUrl", "Game []"},
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean setStream(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            if (args[0].equalsIgnoreCase("NA") || args[1].equalsIgnoreCase("NA")) {
                INIT.BOT.streaming(null, null);
            } else {
                INIT.BOT.streaming(Utils.makeArgsToString(args, new String[] {args[0]}), args[0]);
            }
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
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
            arguments = {"Game []"},
            description = "Change the Bot Game",
            alias = "sp",
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean setplaying(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            if (args[0].equalsIgnoreCase("NA")) {
                INIT.BOT.changePlayingText(null);
            } else {
                INIT.BOT.changePlayingText(Utils.makeArgsToString(args, new String[] {}));
            }
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
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
            arguments = {"Name []"},
            description = "Change the Bot Name",
            alias = "sun",
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean setUsername(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            if (args[0].equalsIgnoreCase("NA")) {
                INIT.BOT.changeUsername(DRIVER.getProperty(DRIVER.CONFIG, "defaultUsername", "MoMuOSB").toString());
            } else {
                INIT.BOT.changeUsername(Utils.makeArgsToString(args, new String[] {}));
            }
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
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
            command = "changeprop",
            arguments = {"Option", "Value"},
            description = "Change the Property",
            alias = "chp",
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean changeProperty(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            if (args[0].equalsIgnoreCase("NA") || args[1].equalsIgnoreCase("NA")) {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+ LANG.getTranslation("notchanged_error")), true);
            } else {
                if (!DRIVER.getPropertyOnly(DRIVER.CONFIG, args[0]).equals("No Value")) {
                    Object value = args[1];
                    boolean error = false;
                    if (args[1].equalsIgnoreCase("false") || args[1].equalsIgnoreCase("true")) {
                        value = Boolean.valueOf(args[1]);
                    }
                    try {
                        if (NumberUtils.isCreatable(args[1])) {
                            value = NumberFormat.getInstance().parse(args[1]);
                        }

                    } catch (Exception ex) {
                        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("parse_error")), true);
                        error = true;
                    }
                    if (!error) {
                        DRIVER.setProperty(DRIVER.CONFIG, args[0], value);
                        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
                    }
                } else {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+ LANG.getTranslation("changeprop_error")), true);
                }
            }
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
        }
        return true;
    }

    @Command(
            command = "printprop",
            arguments = {},
            description = "Print the Property",
            alias = "prp",
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean printProperties(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            EmbedBuilder builder = new EmbedBuilder();
            HashMap<String, Object> objects = DRIVER.getAllKeysWithValues(DRIVER.CONFIG);
            StringBuilder stringBuilder = new StringBuilder();
            for (String string: objects.keySet()) {
                stringBuilder.append(string).append(" = ").append(objects.get(string)).append(" \n");
            }
            builder.appendField(LANG.getTranslation("props"), stringBuilder.toString(), false);

            BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder);
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
        }
        return true;
    }

    @Command(
            command = "changelang",
            arguments = {"Language Code"},
            description = "Change the Language",
            alias = "chlang",
            permission = Permissions.ADMINISTRATOR,
            prefix = Prefix.ADMIN_PREFIX
    )
    public boolean changeLang(MessageReceivedEvent event, String[] args) {
        if (event.getAuthor().equals(INIT.BOT.getApplicationOwner())) {
            changeProperty(event, new String[] {"language", args[0]});
            LANG.createTranslations();
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS+LANG.getTranslation("command_success")), true);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("botowner_error")), true);
        }
        return true;
    }

}
