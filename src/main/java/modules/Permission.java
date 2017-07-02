package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import util.Console;
import util.Prefix;
import util.SMB;
import util.Utils;

import java.util.List;

/**
 * Created by ModdyLP on 01.07.2017. Website: https://moddylp.de/
 */
public class Permission extends Module {

    @Command(
            command = "addperm",
            arguments = {"Permission or Command", "Group"},
            description = "Add Permission to Group",
            permission = Prefix.BOT_PERM,
            prefix = Prefix.ADMIN_PREFIX,
            alias = "addp"
    )
    public void addPermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            boolean success = false;
            Console.debug("Arguments:"+Utils.makeArgsToString(args, new String[] {args[0]}));
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[] {args[0]}));
            if (roles.size() > 0 || checkIfEverone(args)) {
                if (checkIfEverone(args)) {
                    roles.add(event.getGuild().getEveryoneRole());
                }
                for (IRole role : roles) {
                    if (PERM.getStringPermissions().contains(args[0])) {
                        PERM.addPermissionToGroup(role, args[0]);
                        success = true;
                    }
                    if (COMMAND.getAllCommandsAsString().contains(args[0])) {
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
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_success")), true);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_failed")), true);
            }
        } catch (Exception ex) {
            Console.error("Error on adding Permission"+ex.getMessage());
            ex.printStackTrace();
        }

    }
    @Command(
            command = "removeperm",
            arguments = {"Permission or Command","Group []"},
            description = "Remove Permission to Group",
            permission = Prefix.BOT_PERM,
            prefix = Prefix.ADMIN_PREFIX,
            alias = "remp"
    )
    public void removePermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            Console.debug("Arguments:"+Utils.makeArgsToString(args, new String[] {args[0]}));
            boolean success = false;
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[] {args[0]}));
            if (roles.size() > 0 || checkIfEverone(args)) {
                if (checkIfEverone(args)) {
                    roles.add(event.getGuild().getEveryoneRole());
                }
                for (IRole role : roles) {
                    if (PERM.getStringPermissions().contains(args[0])) {
                        PERM.removePermissionToGroup(role, args[0]);
                        success = true;
                    }
                    if (COMMAND.getAllCommandsAsString().contains(args[0])) {
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
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_success")), true);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("perm_failed")), true);
            }
        } catch (Exception ex) {
            Console.error("Error on adding Permission"+ex.getMessage());
            ex.printStackTrace();
        }

    }

    public boolean checkIfEverone(String[] args) {
        return Utils.makeArgsToString(args, new String[]{args[0]}).equalsIgnoreCase("everyone");
    }
}
