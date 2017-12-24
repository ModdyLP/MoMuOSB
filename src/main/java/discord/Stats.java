package discord;

import org.json.JSONArray;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;
import util.Fast;
import util.SMB;
import util.Utils;

import java.util.ArrayList;

/**
 * Created by N.Hartmann on 07.07.2017.
 * Copyright 2017
 */
//GITHUB IDENT MODDYLP
public class Stats implements Fast {
    private static int joineduser = 0;
    private static int joinedserver = 0;
    private static int messages = 0;
    private static int commands = 0;
    private static ArrayList<String> serverlist = new ArrayList<>();
    private static String STATS = "stats.json";


    public static void addUser() {
        joineduser = joineduser + 1;
    }

    public static void addServer(IGuild server) {
        if (!serverlist.contains(server.getStringID())) {
            joinedserver = joinedserver + 1;
            serverlist.add(server.getStringID());
            /*BotUtils.sendPrivEmbMessage(server.getOwner().getOrCreatePMChannel(), SMB.shortMessage("Hello, \n" +
                    "I'm your new Discord bot. \n" +
                    "Type "+DRIVER.getPropertyOnly(DRIVER.CONFIG, "botprefix").toString()+".help \n" +
                    "If you have any Problems then contact me (https://moddylp.de/). \n \n" +
                    "Have a nice Day"), false);*/
        }
    }

    public static void addMessages() {
        messages = messages + 1;
    }

    public static void addCommands() {
        commands = commands + 1;
    }

    public static String getStats() {
        return "Stats: \n" +
                "Server:     "+joinedserver+" \n"+
                "User:      "+joineduser+" \n"+
                "Messages:  "+messages+" \n"+
                "Commands:  "+commands+" \n";
    }


    public static void saveStats() {
        try {
            DRIVER.setProperty(STATS, "joinedUser", joineduser);
            DRIVER.setProperty(STATS, "joinedServer", joinedserver);
            DRIVER.setProperty(STATS, "messages", messages);
            DRIVER.setProperty(STATS, "commands", commands);
            DRIVER.setProperty(STATS, "server", serverlist);
            DRIVER.saveJson();
            Console.println(getStats());
        } catch (Exception ex) {
            Console.error(ex);
        }
    }

    public static void loadStats() {
        try {
            DRIVER.createNewFile(STATS);
            joineduser = Integer.valueOf(DRIVER.getProperty(STATS, "joinedUser", joineduser).toString());
            joinedserver = Integer.valueOf(DRIVER.getProperty(STATS, "joinedServer", joinedserver).toString());
            JSONArray jArray = Utils.objectToJSONArray(DRIVER.getPropertyOnly(STATS, "server"));
            if (jArray != null) {
                for (int i = 0; i < jArray.length(); i++) {
                    serverlist.add(jArray.get(i).toString());
                    Console.debug("Load:  |" + jArray.get(i).toString());
                }
            }
            messages = Integer.valueOf(DRIVER.getProperty(STATS, "messages", messages).toString());
            commands = Integer.valueOf(DRIVER.getProperty(STATS, "commands", commands).toString());
            Console.println(getStats());
        }catch (Exception ex) {
            Console.error(ex);
        }
    }


}
