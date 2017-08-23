package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import org.json.JSONArray;
import org.json.JSONObject;
import storage.LanguageMethod;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import util.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Created by N.Hartmann on 06.07.2017.
 * Copyright 2017
 */
public class RoleManagement extends Module implements Fast {

    public static String female = "f";
    public static String male = "m";
    private static String GENDER = "gender.json";

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
            Console.error(ex);
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
            Console.error(ex);
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
            if (roles.size() > 0) {
                if (args[0].equalsIgnoreCase(female) || (args[0].equalsIgnoreCase(male))) {
                    roletoserver.put(event.getGuild(), roles);
                    gendersave.put(args[0], roletoserver);
                    saveGenders();
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("role_gender_add"), roles.size(), args[0])), true);
                } else {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("role_gender_notfound")), true);
                }
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("role_notfound")), true);
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), Arrays.toString(ex.getStackTrace()))), true);
            Console.error(ex);
        }
    }

    public static boolean isGenderdefined(IGuild guild) {
        return gendersave.get(female).containsKey(guild) && gendersave.get(male).containsKey(guild);
    }

    public static List<IRole> getRoleforGender(IGuild guild, String gender) {
        if (guild == null) {
            Console.error("Guild is null");
        } else {
            return gendersave.get(gender).get(guild);
        }
        return null;
    }

    public static void saveGenders() {
        try {
            Console.println("Saving Roles for RoleManagement");
            JSONObject root = new JSONObject();
            ArrayList<JSONObject> maleserverids = new ArrayList<>();
            ArrayList<JSONObject> femaleserverids = new ArrayList<>();
            if (gendersave.get(female) != null) {
                gendersave.get(female).keySet().forEach(iGuild -> {
                    if (!SERVER_CONTROL.getDisabledlist(SERVER_CONTROL.JOIN_MODULE).contains(iGuild.getStringID())) {
                        ArrayList<String> roleids = new ArrayList<>();
                        for (IRole role: getRoleforGender(iGuild, female)) {
                            roleids.add(role.getStringID());
                        }
                        JSONObject femaleobj = new JSONObject();
                        femaleobj.put(iGuild.getStringID(), roleids);
                        femaleserverids.add(femaleobj);
                    } else {
                        Console.debug("Server is disabled for using Genders: "+iGuild.getName()+ Arrays.toString(SERVER_CONTROL.getDisabledlist(SERVER_CONTROL.JOIN_MODULE).toArray()));
                    }
                });
            }
            if (gendersave.get(male) != null) {
                gendersave.get(male).keySet().forEach(iGuild -> {
                    if (!SERVER_CONTROL.getDisabledlist(SERVER_CONTROL.JOIN_MODULE).contains(iGuild.getStringID())) {
                        ArrayList<String> roleids = new ArrayList<>();
                        for (IRole role: getRoleforGender(iGuild, male)) {
                            roleids.add(role.getStringID());
                        }
                        JSONObject femaleobj = new JSONObject();
                        femaleobj.put(iGuild.getStringID(), roleids);
                        maleserverids.add(femaleobj);
                    } else {
                        Console.debug("Server is disabled for using Genders: "+iGuild.getName()+ Arrays.toString(SERVER_CONTROL.getDisabledlist(SERVER_CONTROL.JOIN_MODULE).toArray()));
                    }
                });
            }
            root.put(female, femaleserverids);
            root.put(male, maleserverids);
            DRIVER.setProperty(GENDER, "gendersave", root);
            DRIVER.saveJson();
        } catch (Exception ex) {
            Console.error(ex);
        }
    }

    public static void loadGenders() {
        try {
            Console.println("Loading Roles for RoleManagement");
            DRIVER.createNewFile(GENDER);
            JSONObject root = Utils.objectToJSONObject(DRIVER.getProperty(GENDER, "gendersave", new JSONObject()));
            if (root.keySet().contains(male) && root.keySet().contains(female)) {
                Console.debug(Utils.crunchifyPrettyJSONUtility(root.toString(4)));
                JSONArray malelist = root.getJSONArray(male);
                JSONArray femalelist = root.getJSONArray(female);
                if (male != null) {
                    putvaluesfromlist(male, malelist);
                }
                if (female != null) {
                    putvaluesfromlist(female, femalelist);
                }
            }
        } catch (Exception ex) {
            Console.error(ex);
        }
    }

    public static void putvaluesfromlist(String gender, JSONArray list) {
        if (list != null) {
            HashMap<IGuild, List<IRole>> serverrolesmap = new HashMap<>();
            for (int i = 0; i < list.length(); i++) {
                JSONObject serverroles = Utils.objectToJSONObject(list.get(i));
                if (serverroles != null) {
                    for (Object serverid : serverroles.keySet()) {
                        JSONArray array = Utils.objectToJSONArray(serverroles.get(serverid.toString()));
                        ArrayList<IRole> roles = new ArrayList<>();
                        for (int j = 0; j < array.length(); j++) {
                            roles.add(INIT.BOT.getRoleByID(Long.valueOf(array.getString(i))));
                        }
                        serverrolesmap.put(INIT.BOT.getGuildByID(Long.valueOf(serverid.toString())), roles);
                    }
                }

            }
            gendersave.put(gender, serverrolesmap);
        }
    }

    @LanguageMethod(
            languagestringcount = 8
    )
    @Override
    public void setdefaultLanguage() {
        //Role Manager
        DRIVER.setProperty(DEF_LANG, "role_add", "Role %1s added successful to User %2s");
        DRIVER.setProperty(DEF_LANG, "role_remove", "Role %1s removed successful to User %2s");
        DRIVER.setProperty(DEF_LANG, "role_gender_add", "Role %1s was added as Gender %2s");
        DRIVER.setProperty(DEF_LANG, "role_gender_notfound", "This Gender is not defined.");
        DRIVER.setProperty(DEF_LANG, "female_ask", "Are you Male or Female? \nAnswer with the Gender like this: \n@%1s f or @%1s m \n f = female, w = male");
        DRIVER.setProperty(DEF_LANG, "gender_role_added", "You get the Role: %1s");
        DRIVER.setProperty(DEF_LANG, "role_notfound", "The Role was not found.");
        DRIVER.setProperty(DEF_LANG, "invalid_count_gender", "Your answer is not well formated. \n Look again on the Question.");
    }
}
