package discord;

import org.json.JSONArray;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;
import util.Utils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by N.Hartmann on 05.07.2017.
 * Copyright 2017
 */
public class ServerControl implements Fast{
    private static ArrayList<String> defaultlist;
    public String MUSIC_MODULE = "music";
    public String JOIN_MODULE = "join";
    public String BAN_MODULE = "banned";
    private static HashMap<String, ArrayList<String>> disabledlist = new HashMap<>();
    private static ServerControl instance;

    public static ServerControl getInstance() {
        if (instance == null) {
            instance = new ServerControl();
        }
        disabledlist.put(instance.MUSIC_MODULE, new ArrayList<>());
        disabledlist.put(instance.BAN_MODULE, new ArrayList<>());
        disabledlist.put(instance.JOIN_MODULE, new ArrayList<>());
        return instance;
    }


    public void addDisabledServer(IGuild guild, boolean newserver, String module) {
        try {
            if (disabledlist.get(module) != null && !disabledlist.get(module).contains(guild.getStringID())) {
                if (newserver) {
                    Console.debug(module+"| Server added as New");
                    disabledlist.get(module).add(guild.getStringID());
                    DRIVER.setProperty(DRIVER.MODULE, module+"_disabled_servers", disabledlist.get(module));
                } else {
                    Console.debug(module+"| Server exists");
                }
                Console.debug("Saved: |"+guild.getStringID()+"   "+module+"    "+guild.getStringID());
            }
        } catch (Exception ex) {
            Console.error(module+"| Cant add Server");
            Console.error(ex);
        }
    }
    public void addBannedServer(String guild) {
        try {
            if (disabledlist.get(BAN_MODULE) != null && !disabledlist.get(BAN_MODULE).contains(guild)) {
                disabledlist.get(BAN_MODULE).add(guild);
                Console.debug("Server added as Banned");
                DRIVER.setProperty(DRIVER.MODULE, BAN_MODULE+"_disabled_servers", disabledlist.get(BAN_MODULE));
                Console.debug("Saved: |"+guild+"   "+BAN_MODULE+"    "+guild);
            }
        } catch (Exception ex) {
            Console.error("Banned| Cant add Server");
            Console.error(ex);
        }
    }
    public void loadSavedServer(String module) {
        DRIVER.createNewFile(DRIVER.MODULE);
        disabledlist.get(module).clear();
        if (DRIVER.hasKey(DRIVER.MODULE, module+"_disabled_servers")) {
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getPropertyOnly(DRIVER.MODULE, module + "_disabled_servers"));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    disabledlist.get(module).add(jArray.get(i).toString());
                    Console.debug("Load:  |" + module + "    " + jArray.get(i).toString());
                }
            }
        }
        Console.debug("Disabled Servers: "+module+" |"+ disabledlist.get(module).size());
    }
    public void removeDisabledServer(IGuild guild, String module) {
        try {
            if (disabledlist.get(module) != null && disabledlist.get(module).contains(guild.getStringID())) {
                disabledlist.get(module).remove(guild.getStringID());
                DRIVER.setProperty(DRIVER.MODULE, module+"_disabled_servers", disabledlist.get(module));
                DRIVER.saveJson();
            }
        } catch (Exception ex) {
            Console.error("Cant remove Server");
            Console.error(ex);
        }
    }
    public boolean checkServerisBanned(IGuild guild) {
        if (disabledlist.get(BAN_MODULE) != null && guild != null) {
            if(disabledlist.get(BAN_MODULE).contains(guild.getStringID())) {
                Console.debug("Found: |" + guild.getStringID() + "   " + BAN_MODULE + "    " + guild.getStringID());
                return true;
            }
        }
        return false;
    }
    public ArrayList<String> getDisabledlist(String module) {

        return disabledlist.get(module);
    }
}
