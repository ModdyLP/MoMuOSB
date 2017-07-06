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
public class ServerControl implements Fast{
    public String MUSIC_MODULE = "music";
    public String JOIN_MODULE = "joinevent";
    private static HashMap<String, ArrayList<String>> disabledlist = new HashMap<>();
    private static ServerControl instance;

    public static ServerControl getInstance() {
        if (instance == null) {
            instance = new ServerControl();
        }
        return instance;
    }


    public void addDisabledServer(IGuild guild, boolean newserver, String module) {
        try {
            if (disabledlist.get(module) != null && !disabledlist.get(module).contains(guild.getStringID())) {
                disabledlist.get(module).add(guild.getStringID());
                if (newserver) {
                    DRIVER.setProperty(DRIVER.CONFIG, module+"_disabled_servers", disabledlist.get(module));
                } else {
                    DRIVER.getProperty(DRIVER.CONFIG, module+"_disabled_servers", disabledlist.get(module));
                }
                Console.debug("Saved: |"+guild.getStringID()+"   "+module+"    "+guild.getStringID());
            }
        } catch (Exception ex) {
            Console.error("Cant add Server");
            ex.printStackTrace();
        }
    }
    public void loadSavedServer(String module) {
        disabledlist.computeIfAbsent(module, k -> new ArrayList<>());
        disabledlist.get(module).clear();
        JSONArray jArray = Utils.objectToJSONArray(DRIVER.getProperty(DRIVER.CONFIG, module+"_disabled_servers", new ArrayList<String>()));
        if (jArray != null) {
            for (int i = 0; i < jArray.length(); i++) {
                disabledlist.get(module).add(jArray.get(i).toString());
                Console.debug("Load:  |"+module+"    "+jArray.get(i).toString());
            }
        }
    }
    public void removeDisabledServer(IGuild guild, String module) {
        try {
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getProperty(DRIVER.CONFIG, module+"_disabled_servers", new ArrayList<String>()));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    disabledlist.get(module).add(jArray.get(i).toString());
                }
            }
            if (disabledlist.get(module) != null && disabledlist.get(module).contains(guild.getStringID())) {
                disabledlist.get(module).remove(guild.getStringID());
                DRIVER.setProperty(DRIVER.CONFIG, module+"_disabled_servers", disabledlist.get(module));
                DRIVER.saveJson();
            }
        } catch (Exception ex) {
            Console.error("Cant remove Server");
            ex.printStackTrace();
        }
    }
    public ArrayList<String> getDisabledlist(String module) {
        disabledlist.computeIfAbsent(module, k -> new ArrayList<>());
        return disabledlist.get(module);
    }
}
