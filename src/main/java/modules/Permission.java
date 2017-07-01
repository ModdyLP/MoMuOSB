package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IRole;
import util.Console;
import util.Prefix;
import util.SMB;

import java.util.List;

/**
 * Created by ModdyLP on 01.07.2017. Website: https://moddylp.de/
 */
public class Permission extends Module {

    @Command(
            command = "addperm",
            arguments = {"Group", "Permission or Command"},
            description = "Add Permission to Group",
            permission = "manageperm",
            prefix = Prefix.ADMIN_PREFIX,
            alias = "addp"
    )
    public void addPermToGroup(MessageReceivedEvent event, String[] args) {
        try {
            boolean success = false;
            List<IRole> roles = event.getGuild().getRolesByName(args[0]);
            if (roles.size() > 0) {
                for (IRole role : roles) {
                    if (PERM.getStringPermissions().contains(args[1])) {
                        PERM.addPermissionToGroup(role, args[1]);
                        success = true;
                    }
                    if (COMMAND.getAllCommandsAsString().contains(args[1])) {
                        Command command = COMMAND.getCommandByName(args[1]);
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
}
