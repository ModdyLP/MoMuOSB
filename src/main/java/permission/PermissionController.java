package permission;

import events.Command;
import org.json.simple.JSONArray;
import org.omg.CORBA.IRObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import sx.blah.discord.handle.obj.Permissions;
import util.Console;
import util.Fast;
import util.Prefix;

import java.util.*;

/**
 * Created by ModdyLP on 01.07.2017. Website: https://moddylp.de/
 */
public class PermissionController implements Fast {

    private static PermissionController instance;
    private static String PERMFILE = "permissions.json";

    public static PermissionController getInstance() {
        if (instance == null) {
            instance = new PermissionController();
        }
        return instance;
    }

    private HashMap<Command, String> permissions = new HashMap<>();
    private HashMap<IRole, ArrayList<String>> grouppermissions = new HashMap<>();

    public void addPermission(Command command) {
        permissions.put(command, command.permission());
    }

    public boolean hasPermission(IUser user, IGuild server, String permission) {
        boolean check = false;
        if (permission.equalsIgnoreCase(Prefix.BOT_OWNER)) {
            return user.equals(INIT.BOT.getApplicationOwner());
        } else {
            for (IRole role : user.getRolesForGuild(server)) {
                if (grouppermissions.get(role) != null && grouppermissions.get(role).contains(permission)) {
                    check = true;
                }
            }
        }
        return check;
    }

    public IRole groupPermission(String permission) {
        for (IRole role : grouppermissions.keySet()) {
            if (!role.isDeleted()) {
                if (grouppermissions.get(role).contains(permission)) {
                    return role;
                }
            }
        }
        return null;
    }

    public int getAccessAmount(IUser user, IGuild server) {
        int count = 0;
        for (Command command : COMMAND.getAllCommands()) {
            if (hasPermission(user, server, command.permission())) {
                count++;
            }
        }
        return count;
    }

    public void addPermissionToGroup(IRole role, String permission) {
        if (permissions.containsValue(permission)) {
            ArrayList<String> grouppermission = grouppermissions.get(role);
            if (grouppermission == null) {
                grouppermission = new ArrayList<>();
            }
            grouppermission.add(permission);
            grouppermissions.put(role, grouppermission);
        } else {
            Console.debug("This permission doesnt exist: "+permission);
        }
        savePermissions();
    }
    public void removePermissionToGroup(IRole role, String permission) {
        if (permissions.containsValue(permission)) {
            ArrayList<String> grouppermission = grouppermissions.get(role);
            if (grouppermission == null) {
                return;
            }
            grouppermission.remove(permission);
        } else {
            Console.debug("This permission doesnt exist: "+permission);
        }
        savePermissions();
    }
    public void removePermissionToGroup(IRole role, Command command) {
        ArrayList<String> grouppermission = grouppermissions.get(role);
        if (grouppermission == null) {
            return;
        }
        grouppermission.remove(command.permission());
        savePermissions();
    }

    public void addPermissionToGroup(IRole role, Command command) {
        ArrayList<String> grouppermission = grouppermissions.get(role);
        if (grouppermission == null) {
            grouppermission = new ArrayList<>();
        }
        grouppermission.add(command.permission());
        grouppermissions.put(role, grouppermission);
        savePermissions();
    }

    public ArrayList<String> getStringPermissions() {
        ArrayList<String> permission = new ArrayList<>();
        permission.addAll(permissions.values());
        return permission;
    }

    public HashMap<Command, String> getPermissions() {
        return permissions;
    }

    public HashMap<IRole, ArrayList<String>> getGrouppermissions() {
        return grouppermissions;
    }

    private void savePermissions() {
        try {
            DRIVER.createNewFile(PERMFILE);
            for (IRole role : grouppermissions.keySet()) {
                ArrayList<String> permissions = grouppermissions.get(role);
                DRIVER.setProperty(PERMFILE, String.valueOf(role.getLongID()), permissions);
            }
            DRIVER.saveJson();
        } catch (Exception ex) {
            Console.error("Saving of Permissions failed");
            ex.printStackTrace();
        }
    }

    public void loadPermissions(List<IGuild> server) {
        try {
            DRIVER.createNewFile(PERMFILE);
            HashMap<String, Object> values = DRIVER.getAllKeysWithValues(PERMFILE);
            for (String roleid : values.keySet()) {
                for (IGuild serverinstance : server) {
                    IRole role = serverinstance.getRoleByID(Long.valueOf(roleid));
                    if (role != null) {
                        ArrayList<String> permission = new ArrayList<String>();
                        JSONArray jArray = (JSONArray) values.get(roleid);
                        if (jArray != null) {
                            for (Object aJArray : jArray) {
                                permission.add(aJArray.toString());
                            }
                        }
                        grouppermissions.put(role, permission);
                        Console.debug(role.getLongID() + " " + role.getName() + " " + permission.size()+"   "+grouppermissions.size());
                        for (String perm : permission) {
                            Console.debug("Permission: " + perm);
                            permissions.put(COMMAND.getCommandByPermission(perm), perm);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            Console.error("Failed to load Permissions");
            ex.printStackTrace();
        }
    }


    public void setDefaultPermissions(List<IGuild> server) {
        Console.debug("Load default permissions...");
        if (!DRIVER.checkIfFileExists(PERMFILE) && DRIVER.checkIfFileisEmpty(PERMFILE)) {
            List<IRole> adminroles = new ArrayList<>();
            List<IRole> everyoneroles = new ArrayList<>();
            for (IGuild serverinst : server) {
                for (IRole role : serverinst.getRoles()) {
                    if (role.getPermissions().contains(Permissions.ADMINISTRATOR)) {
                        adminroles.add(role);
                    }
                }
                everyoneroles.add(serverinst.getEveryoneRole());
            }
            for (IRole role : adminroles) {
                PERM.addPermissionToGroup(role, Prefix.BOT_MANAGE);
            }
            for (IRole role : everyoneroles) {
                PERM.addPermissionToGroup(role, Prefix.BOT_INFO);
            }
            DRIVER.saveJson();
            Console.debug("Permission loaded: Admin:" + adminroles.size() + " Info: " + everyoneroles.size());
        }
    }
}
