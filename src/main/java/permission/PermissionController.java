package permission;

import events.Command;
import org.omg.CORBA.IRObject;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IRole;
import sx.blah.discord.handle.obj.IUser;
import util.Console;
import util.Fast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

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
        Console.debug("Permissions added for Command: " + command.permission());
    }

    public boolean hasPermission(List<IRole> roles, String permission) {
        boolean check = false;
        for (IRole role : roles) {
            if (grouppermissions.get(role) != null && grouppermissions.get(role).contains(permission)) {
                check = true;
            }
        }
        return check;
    }

    public IRole groupPermission(String permission) {
        for (IRole role : grouppermissions.keySet()) {
            if (grouppermissions.get(role).contains(permission)) {
                return role;
            }
        }
        return null;
    }

    public int getAccessAmount(List<IRole> role, IUser user) {
        int count = 0;
        for (Command command : COMMAND.getAllCommands()) {
            if (hasPermission(role, command.permission()) || user.equals(INIT.BOT.getApplicationOwner())) {
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
                    ArrayList<String> permission = (ArrayList<String>) values.get(roleid);
                    if (permission != null) {
                        grouppermissions.put(role, permission);
                        for (String perm : permission) {
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
}
