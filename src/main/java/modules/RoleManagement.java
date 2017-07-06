package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import org.json.JSONArray;
import org.json.JSONObject;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import util.Console;
import util.Globals;
import util.SMB;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by N.Hartmann on 06.07.2017.
 * Copyright 2017
 */
public class RoleManagement extends Module {

    public static String female = "f";
    public static String male = "m";

    private static HashMap<String, HashMap<IGuild, List<IRole>>> gendersave = new HashMap<>();

    @Command(
            command = "addUserRole",
            description = "Add a User to a specific role",
            arguments = {"User Mention", "Role []"},
            prefix = Globals.ADMIN_PREFIX,
            permission = "role_manage",
            alias = "aur"
    )
    public void addUsertoRole(MessageReceivedEvent event, String[] args) {
        try {
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{args[0]}));
            for (IUser user : event.getMessage().getMentions()) {
                for (IRole role : roles) {
                    user.addRole(role);
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("role_add"), role.getName(), user.getName())), true);
                }
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace()))), true);
            ex.printStackTrace();
        }
    }

    @Command(
            command = "removeUserRole",
            description = "Add a User to a specific role",
            arguments = {"User Mention", "Role []"},
            prefix = Globals.ADMIN_PREFIX,
            permission = "role_manage",
            alias = "rmur"
    )
    public void removeUsertoRole(MessageReceivedEvent event, String[] args) {
        try {
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{args[0]}));
            for (IUser user : event.getMessage().getMentions()) {
                for (IRole role : roles) {
                    user.removeRole(role);
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("role_remove"), role.getName(), user.getName())), true);
                }
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace()))), true);
            ex.printStackTrace();
        }
    }

    @Command(
            command = "definegenderrole",
            description = "Add a User to a specific role",
            arguments = {"Gender (m or w)", "Role []"},
            prefix = Globals.ADMIN_PREFIX,
            permission = "role_manage",
            alias = "dgr"
    )
    public void setGenderRole(MessageReceivedEvent event, String[] args) {
        try {
            HashMap<IGuild, List<IRole>> roletoserver = new HashMap<>();
            List<IRole> roles = event.getGuild().getRolesByName(Utils.makeArgsToString(args, new String[]{args[0]}));
            if (args[0].equalsIgnoreCase(female) || (args[0].equalsIgnoreCase(male))) {
                roletoserver.put(event.getGuild(), roles);
                gendersave.put(args[0], roletoserver);
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("role_gender_add"), roles.size(), args[0])), true);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("role_gender_notfound")), true);
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace()))), true);
            ex.printStackTrace();
        }
    }

    public static boolean isGenderdefined(IGuild guild) {
        return gendersave.get(female).containsKey(guild) && gendersave.get(male).containsKey(guild);
    }

    public static List<IRole> getRoleforGender(IGuild guild, String gender) {
        return gendersave.get(gender).get(guild);
    }

    public static void saveGenders() {
        try {
            Console.println("Saving Roles for RoleManagement");
            if (DRIVER.getPropertyOnly(DRIVER.CONFIG, "genderroles").equals(true)) {
                JSONObject root = new JSONObject();
                ArrayList<JSONObject> maleserverids = new ArrayList<>();
                ArrayList<JSONObject> femaleserverids = new ArrayList<>();
                if (gendersave.get(female) != null) {
                    gendersave.get(female).keySet().forEach(iGuild -> {
                        JSONObject femaleobj = new JSONObject();
                        femaleobj.append(iGuild.getStringID(), getRoleforGender(iGuild, female));
                        femaleserverids.add(femaleobj);
                    });
                }
                if (gendersave.get(male) != null) {
                    gendersave.get(male).keySet().forEach(iGuild -> {
                        JSONObject femaleobj = new JSONObject();
                        femaleobj.append(iGuild.getStringID(), getRoleforGender(iGuild, male));
                        maleserverids.add(femaleobj);
                    });
                }
                root.append(female, femaleserverids);
                root.append(male, maleserverids);
                DRIVER.setProperty(DRIVER.CONFIG, "gendersave", root);
                DRIVER.saveJson();
            }  else {
                Console.error("Gender Module disabled");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void loadGenders() {
        try {
            Console.println("Loading Roles for RoleManagement");
            if (DRIVER.getPropertyOnly(DRIVER.CONFIG, "genderroles").equals(true)) {
                ArrayList<JSONObject> maleserverids = new ArrayList<>();
                ArrayList<JSONObject> femaleserverids = new ArrayList<>();
                JSONObject root = Utils.objectToJSONObject(DRIVER.getProperty(DRIVER.CONFIG, "gendersave", new JSONObject()));
                Console.debug(Utils.crunchifyPrettyJSONUtility(root.toString(4)));
                JSONArray malelist = root.getJSONArray(male);
                JSONArray femalelist = root.getJSONArray(female);
                if (male != null) {
                    putvaluesfromlist(male, malelist);
                }
                if (female != null) {
                    putvaluesfromlist(female, femalelist);
                }
            } else {
                Console.error("Gender Module disabled");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void putvaluesfromlist(String gender, JSONArray list) {
        if (list != null) {
            HashMap<IGuild, List<IRole>> serverrolesmap = new HashMap<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject serverroles = Utils.objectToJSONObject(list.get(i));
                for (Object serverid : serverroles.keySet()) {
                    JSONArray array = Utils.objectToJSONArray(serverroles.get(serverid.toString()));
                    ArrayList<IRole> roles = new ArrayList<>();
                    for (int j = 0; j < array.length(); j++) {
                        roles.add(INIT.BOT.getRoleByID(Long.valueOf(array.getString(i))));
                    }
                    serverrolesmap.put(INIT.BOT.getGuildByID(Long.valueOf(serverid.toString())), roles);
                }

            }
            gendersave.put(gender, serverrolesmap);
        }
    }

}
