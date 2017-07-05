package discord;

import org.json.JSONArray;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;
import util.Utils;

import java.util.ArrayList;

/**
 * Created by N.Hartmann on 05.07.2017.
 * Copyright 2017
 */
public class ServerControl implements Fast{
    private static ArrayList<String> disabledserverslist = new ArrayList<>();

    public static void addDisabledServer(IGuild guild, boolean newserver) {
        try {
            disabledserverslist.clear();
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getProperty(DRIVER.CONFIG, "music_disabled_servers", new ArrayList<String>()));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    disabledserverslist.add(jArray.get(i).toString());
                }
            }
            if (!disabledserverslist.contains(guild.getStringID())) {
                disabledserverslist.add(guild.getStringID());
                if (newserver) {
                    DRIVER.setProperty(DRIVER.CONFIG, "music_disabled_servers", disabledserverslist);
                } else {
                    DRIVER.getProperty(DRIVER.CONFIG, "music_disabled_servers", disabledserverslist);
                }
                DRIVER.saveJson();
            }
        } catch (Exception ex) {
            Console.error("Cant add Server");
            ex.printStackTrace();
        }
    }

    public static void removeDisabledServer(IGuild guild) {
        try {
            disabledserverslist.clear();
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getProperty(DRIVER.CONFIG, "music_disabled_servers", new ArrayList<String>()));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    disabledserverslist.add(jArray.get(i).toString());
                }
            }
            if (disabledserverslist.contains(guild.getStringID())) {
                disabledserverslist.remove(guild.getStringID());
                DRIVER.setProperty(DRIVER.CONFIG, "music_disabled_servers", disabledserverslist);
                DRIVER.saveJson();
            }
        } catch (Exception ex) {
            Console.error("Cant remove Server");
            ex.printStackTrace();
        }
    }
    public static ArrayList<String> getDisabledserverslist() {
        return disabledserverslist;
    }
}
