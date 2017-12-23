package discord;

import org.json.JSONArray;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;
import util.Utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by N.Hartmann on 05.07.2017.
 * Copyright 2017
 */
public class ServerControl implements Fast {
    public String MUSIC_MODULE = "music";
    public String JOIN_MODULE = "join";
    public String BAN_MODULE = "banned";
    private static HashMap<String, ArrayList<String>> enabledList = new HashMap<>();
    private static ServerControl instance;

    public static ServerControl getInstance() {
        if (instance == null) {
            instance = new ServerControl();
        }
        enabledList.put(instance.MUSIC_MODULE, new ArrayList<>());
        enabledList.put(instance.BAN_MODULE, new ArrayList<>());
        enabledList.put(instance.JOIN_MODULE, new ArrayList<>());
        return instance;
    }


    public void addEnabledServer(IGuild guild, String module) {
        try {
            if (enabledList.get(module) != null && !enabledList.get(module).contains(guild.getStringID())) {
                Console.debug(module + "| Server added as New");
                enabledList.get(module).add(guild.getStringID());
                DRIVER.setProperty(DRIVER.MODULE, module + "_enabled_servers", enabledList.get(module));
                Console.debug("Saved: |" + guild.getStringID() + "   " + module + "    " + guild.getStringID());
            }
        } catch (Exception ex) {
            Console.error(module + "| Cant add Server");
            Console.error(ex);
        }
    }

    public void addBannedServer(String guild) {
        try {
            if (enabledList.get(BAN_MODULE) != null && !enabledList.get(BAN_MODULE).contains(guild)) {
                enabledList.get(BAN_MODULE).add(guild);
                Console.debug("Server added as Banned");
                DRIVER.setProperty(DRIVER.MODULE, BAN_MODULE + "_enabled_servers", enabledList.get(BAN_MODULE));
                Console.debug("Saved: |" + guild + "   " + BAN_MODULE + "    " + guild);
            }
        } catch (Exception ex) {
            Console.error("Banned| Cant add Server");
            Console.error(ex);
        }
    }

    public void loadSavedServer(String module) {
        DRIVER.createNewFile(DRIVER.MODULE);
        enabledList.get(module).clear();
        if (DRIVER.hasKey(DRIVER.MODULE, module + "_enabled_servers")) {
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getPropertyOnly(DRIVER.MODULE, module + "_enabled_servers"));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    enabledList.get(module).add(jArray.get(i).toString());
                    Console.debug("Load:  |" + module + "    " + jArray.get(i).toString());
                }
            }
        }
        Console.debug("Disabled Servers: " + module + " |" + enabledList.get(module).size());
    }

    public void removeEnabledServer(IGuild guild, String module) {
        try {
            if (enabledList.get(module) != null && enabledList.get(module).contains(guild.getStringID())) {
                enabledList.get(module).remove(guild.getStringID());
                DRIVER.setProperty(DRIVER.MODULE, module + "_enabled_servers", enabledList.get(module));
                DRIVER.saveJson();
            }
        } catch (Exception ex) {
            Console.error("Cant remove Server");
            Console.error(ex);
        }
    }

    public boolean checkServerisBanned(IGuild guild) {
        if (enabledList.get(BAN_MODULE) != null && guild != null) {
            if (enabledList.get(BAN_MODULE).contains(guild.getStringID())) {
                Console.debug("Banned: |" + guild.getStringID() + "   " + BAN_MODULE + "    " + guild.getStringID());
                return true;
            }
        }
        return false;
    }

    public ArrayList<String> getEnabledList(String module) {

        return enabledList.get(module);
    }
}
