package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import storage.LanguageInterface;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.util.EmbedBuilder;
import util.*;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ModdyLP on 01.07.2017. Website: https://moddylp.de/
 */
public class Permission extends Module implements Fast {

    @Command(
            command = "addperm",
            arguments = {"Permission or Command", "Group"},
            description = "Add Permission to Group",
            permission = Globals.BOT_PERM,
            prefix = Globals.ADMIN_PREFIX,
            alias = "addp"
    )
    public void addPermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            boolean success = false;
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{args[0]}));
            if (roles.size() > 0 || checkIfEverone(args)) {
                if (checkIfEverone(args)) {
                    roles.add(event.getGuild().getEveryoneRole());
                }
                for (IRole role : roles) {
                    if (PERM.getStringPermissions().contains(args[0])) {
                        PERM.addPermissionToGroup(role, args[0]);
                        success = true;
                    }
                    if (COMMAND.getAllCommandsAsString().contains(args[0]) || COMMAND.getAllAliasAsString().contains(args[0])) {
                        Command command = COMMAND.getCommandByName(args[0]);
                        if (command != null) {
                            PERM.addPermissionToGroup(role, command);
                            success = true;
                        }
                    }
                }
            } else {
                Console.debug("Role size 0");
            }
            if (success) {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_add_success")), true);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_add_failed")), true);
            }
        } catch (Exception ex) {
            Console.error("Error on adding Permission" + ex.getMessage());
            Console.error(ex);
        }

    }

    @Command(
            command = "removeperm",
            arguments = {"Permission or Command", "Group []"},
            description = "Remove Permission to Group",
            permission = Globals.BOT_PERM,
            prefix = Globals.ADMIN_PREFIX,
            alias = "remp"
    )
    public void removePermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            boolean success = false;
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{args[0]}));
            if (roles.size() > 0 || checkIfEverone(args)) {
                if (checkIfEverone(args)) {
                    roles.add(event.getGuild().getEveryoneRole());
                }
                for (IRole role : roles) {
                    if (PERM.getStringPermissions().contains(args[0])) {
                        PERM.removePermissionToGroup(role, args[0]);
                        success = true;
                    }
                    if (COMMAND.getAllCommandsAsString().contains(args[0]) || COMMAND.getAllAliasAsString().contains(args[0])) {
                        Command command = COMMAND.getCommandByName(args[0]);
                        if (command != null) {
                            PERM.removePermissionToGroup(role, command);
                            success = true;
                        }
                    }
                }
            } else {
                Console.debug("Role size 0");
            }
            if (success) {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_rem_success")), true);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_rem_failed")), true);
            }
        } catch (Exception ex) {
            Console.error("Error on adding Permission" + ex.getMessage());
            Console.error(ex);
        }

    }

    @Command(
            command = "listperm",
            arguments = {"Group []"},
            description = "List Permission to Group",
            permission = Globals.BOT_PERM,
            prefix = Globals.ADMIN_PREFIX,
            alias = "listp"
    )
    public void listPermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{}));
            if (roles.size() > 0 || checkIfEveroneZwei(args)) {
                if (checkIfEveroneZwei(args)) {
                    roles.add(event.getGuild().getEveryoneRole());
                }
                buildMessage(roles, event);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("norolefound")), true);
            }
        } catch (Exception ex) {
            Console.error("Error on adding Permission" + ex.getMessage());
            Console.error(ex);
        }

    }

    @Command(
            command = "printperm",
            arguments = {"ServerID"},
            description = "Print Perm for specific Server",
            permission = Globals.BOT_PERM,
            prefix = Globals.ADMIN_PREFIX,
            alias = "printp"
    )
    public void printoutgroups(MessageReceivedEvent event, String[] args) {
        try {
            List<IRole> roles = INIT.BOT.getGuildByID(Long.valueOf(args[0])).getRoles();
            if (roles.size() > 0) {
                roles.add(INIT.BOT.getGuildByID(Long.valueOf(args[0])).getEveryoneRole());
                buildMessage(roles, event);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("norolefound")), true);
            }

        } catch (Exception ex) {
            Console.error("Error on adding Permission" + ex.getMessage());
            Console.error(ex);
        }

    }

    public boolean checkIfEverone(String[] args) {
        return Utils.makeArgsToString(args, new String[]{args[0]}).equalsIgnoreCase("everyone");
    }

    public boolean checkIfEveroneZwei(String[] args) {
        return Utils.makeArgsToString(args, new String[]{}).equalsIgnoreCase("everyone");
    }

    @LanguageMethod(
            languagestringcount = 6
    )
    @Override
    public void setdefaultLanguage() {
        //Permission
        DRIVER.setProperty(DEF_LANG, "perm_add_success", "Permission added successful.");
        DRIVER.setProperty(DEF_LANG, "perm_add_failed", "Failed to add Permission to group.");
        DRIVER.setProperty(DEF_LANG, "perm_rem_success", "Permission removed successful.");
        DRIVER.setProperty(DEF_LANG, "perm_rem_failed", "Failed to remove Permission to group.");
        DRIVER.setProperty(DEF_LANG, "permlist_title", "Permission List for Group %1s");
        DRIVER.setProperty(DEF_LANG, "norolefound", "The Role was not found.");
    }

    private void buildMessage(List<IRole> roles, MessageReceivedEvent event) {
        ArrayList<EmbedBuilder> builders = new ArrayList<>();
        int page = 1;
        EmbedBuilder builder = new EmbedBuilder();
        builders.add(page - 1, builder);
        builders.get(page - 1).withColor(Color.CYAN);
        for (IRole role : roles) {
            ArrayList<String> permissions = PERM.getGrouppermissions().get(role);
            builders.get(page - 1).withTitle(String.format(LANG.getTranslation("permlist_title"),
                    role.getName()+"   "+role.getGuild().getName()));
            for (String permission : permissions) {
                for (Command command : COMMAND.getCommandByPermission(permission)) {
                    builders.get(page - 1).appendField(LANG.getTranslation("help_command") +
                                    ": " + command.command(),
                            LANG.getTranslation("help_permission") + ": " + permission, false);
                    if (builders.get(page - 1).getFieldCount() == EmbedBuilder.FIELD_COUNT_LIMIT) {
                        EmbedBuilder buildertemp = new EmbedBuilder();
                        builders.add(page - 1, buildertemp);
                        page++;
                    }
                }
            }
        }
        for (EmbedBuilder builderinst : builders) {
            BotUtils.sendPrivEmbMessage(event.getAuthor().getOrCreatePMChannel(), builderinst, false);
        }
    }
}
