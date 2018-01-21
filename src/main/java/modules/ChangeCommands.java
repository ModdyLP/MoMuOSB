package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import events.UserEvents;
import storage.LanguageInterface;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.obj.Embed;
import sx.blah.discord.handle.obj.IEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IUser;
import util.*;
import org.apache.commons.lang3.math.NumberUtils;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.Image;

import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Created by N.Hartmann on 28.06.2017.
 * Copyright 2017
 */
public class ChangeCommands extends Module implements Fast {


    @Command(
            command = "testuser",
            description = "TestUserJoin",
            alias = "testu",
            arguments = {"UserMention[]"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean testuser(MessageReceivedEvent event, String[] args) {
       if (event.getMessage().getMentions().size() > 0) {
            for (IUser user: event.getMessage().getMentions()) {
                UserEvents.getInstance().sendAwaittoUser(event.getGuild(), user);
            }
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("You should mention at least one User."), true);
        }
        return true;
    }
    /**
     * Change the Avatar of the Bot
     *
     * @param event MessageEvent
     * @param args  Argumente
     * @return state
     */
    @Command(
            command = "setavatar",
            description = "Change the Bot Avatar",
            alias = "sa",
            arguments = {"ImageLink"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean changeAvatar(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("NA")) {
            INIT.BOT.changeAvatar(Image.defaultAvatar());

        } else {
            INIT.BOT.changeAvatar(Image.forUrl("png", args[0]));
        }
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    /**
     * Change the Stream of the Bot
     *
     * @param event MessageEvent
     * @param args  Argumente
     * @return state
     */
    @Command(
            command = "setstream",
            description = "Change the Bot Stream",
            alias = "sst",
            arguments = {"StreamUrl", "Game []"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean setStream(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("NA") || args[1].equalsIgnoreCase("NA")) {
            INIT.BOT.streaming(null, null);
        } else {
            INIT.BOT.streaming(Utils.makeArgsToString(args, new String[]{args[0]}), args[0]);
        }
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    /**
     * Change the Game of the Bot
     *
     * @param event MessageEvent
     * @param args  Argumente
     * @return state
     */
    @Command(
            command = "setplaying",
            arguments = {"Game []"},
            description = "Change the Bot Game",
            alias = "sp",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean setplaying(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("NA")) {
            INIT.BOT.changePlayingText(null);
        } else {
            INIT.BOT.changePlayingText(Utils.makeArgsToString(args, new String[]{}));
        }
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    /**
     * Change the Username of the Bot
     *
     * @param event MessageEvent
     * @param args  Argumente
     * @return state
     */
    @Command(
            command = "setusername",
            arguments = {"Name []"},
            description = "Change the Bot Name",
            alias = "sun",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean setUsername(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("NA")) {
            INIT.BOT.changeUsername(DRIVER.getPropertyOnly(DRIVER.CONFIG, "defaultUsername").toString());
        } else {
            INIT.BOT.changeUsername(Utils.makeArgsToString(args, new String[]{}));
        }
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    /**
     * Change the Username of the Bot
     *
     * @param event MessageEvent
     * @param args  Argumente
     * @return state
     */
    @Command(
            command = "changeprop",
            arguments = {"Option", "Value"},
            description = "Change the Property",
            alias = "chp",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean changeProperty(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("NA") || args[1].equalsIgnoreCase("NA")) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("notchanged_error")), true);
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
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
                }
                DRIVER.saveJson();
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR + LANG.getTranslation("changeprop_error")), true);
            }
        }
        return true;
    }

    @Command(
            command = "printprop",
            arguments = {},
            description = "Print the Property",
            alias = "prp",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean printProperties(MessageReceivedEvent event, String[] args) {
        DRIVER.loadJson();
        EmbedBuilder builder = new EmbedBuilder();
        HashMap<String, Object> objects = DRIVER.getAllKeysWithValues(DRIVER.CONFIG);
        StringBuilder stringBuilder = new StringBuilder();
        for (String string : objects.keySet()) {
            stringBuilder.append(string).append(" = ").append(objects.get(string)).append(" \n");
        }
        builder.appendField(LANG.getTranslation("props"), stringBuilder.toString(), false);

        BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builder, false);
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    @Command(
            command = "changelang",
            arguments = {"Language Code"},
            description = "Change the Language",
            alias = "chlang",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean changeLang(MessageReceivedEvent event, String[] args) {
        changeProperty(event, new String[]{"language", args[0]});
        LANG.createTranslations();
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    @Command(
            command = "changestatus",
            arguments = {"Status"},
            description = "Change the Status",
            alias = "chst",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean changeStatus(MessageReceivedEvent event, String[] args) {
        if (args[0].equalsIgnoreCase("idle")) {
            INIT.BOT.idle();
        } else {
            INIT.BOT.online();
        }
        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        return true;
    }

    @Command(
            command = "leaveserver",
            arguments = {"ServerID"},
            description = "Leaves a specific Server",
            alias = "leaves",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean leaveServer(MessageReceivedEvent event, String[] args) {
        try {
            IGuild guild = INIT.BOT.getGuildByID(Long.valueOf(args[0]));
            guild.leave();
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.SUCCESS + LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
        }
        return true;
    }

    @Command(
            command = "muteserver",
            arguments = {"ServerID", "true or false"},
            description = "Mutes a specific Server",
            alias = "mutes",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean muteServer(MessageReceivedEvent event, String[] args) {
        try {
            IGuild guild = INIT.BOT.getGuildByID(Long.valueOf(args[0]));
            INIT.BOT.mute(guild, Boolean.valueOf(args[1]));
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.SUCCESS + LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
        }
        return true;
    }

    @Command(
            command = "reloadconfig",
            arguments = {},
            description = "Reload the Config",
            alias = "relcon",
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean reloadConfig(MessageReceivedEvent event, String[] args) {
        try {
            DRIVER.loadJson();
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.SUCCESS + LANG.getTranslation("command_success")), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.SUCCESS + LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
        }
        return true;
    }

    @Command(
            command = "getLogs",
            description = "Get the Logs of the Bot",
            alias = "gL",
            arguments = {"DayCount"},
            permission = Globals.BOT_OWNER,
            prefix = Globals.ADMIN_PREFIX
    )
    public boolean getLogs(MessageReceivedEvent event, String[] args) {
        try {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("Logs were send to your private messages"), true);
            if (args.length > 0) {
                IMessage message = BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage("The Logs of the last 7 days"), false);
                BotUtils.addReactionToMessage(message, "x");
                File dir = new File("./logs");
                int days = Integer.valueOf(args[0]);
                int logs = 0;
                if (dir.isDirectory()) {
                    List<File> list = Arrays.asList(Objects.requireNonNull(dir.listFiles()));
                    for (File file : list) {
                        IMessage test = event.getAuthor().getOrCreatePMChannel().sendFile(file.getName(), file);
                        BotUtils.addReactionToMessage(test, "x");
                        logs++;
                        if (logs == days) {
                            break;
                        }
                    }
                }
            } else {
                BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage("Please provide a valid Count of Days"), true);
            }
        } catch (Exception ex) {
            BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), SMB.shortMessage("The Creation of the Logs failed"), true);
        }
        return true;
    }


    @LanguageMethod(
            languagestringcount = 4
    )
    @Override
    public void setdefaultLanguage() {
        //Changes
        DRIVER.setProperty(DEF_LANG, "changeprop_error", "This option can't found in the config file!");
        DRIVER.setProperty(DEF_LANG, "notchanged_error", "The change Command does not provide resetting.");
        DRIVER.setProperty(DEF_LANG, "parse_error", "The Value cant parsed into a valid format.");
        DRIVER.setProperty(DEF_LANG, "props", "Properties");
    }
}
