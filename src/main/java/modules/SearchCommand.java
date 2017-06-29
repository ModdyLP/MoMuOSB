package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import org.json.JSONArray;
import org.json.JSONObject;
import storage.RestRequest;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;
import util.Console;
import util.ShortMessageBuilder;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ModdyLP on 29.06.2017. Website: https://moddylp.de/
 */
public class SearchCommand extends Module{

    HashMap<IGuild, ArrayList<String>> links = new HashMap<>();
    HashMap<IGuild, Integer> searchmarker = new HashMap<>();


    @Command(
            command = "search",
            description = "Search a Image",
            alias = "sq",
            arguments = {"SearchEngine(Google)","Query Comma Seperated"},
            permission = Permissions.EMBED_LINKS
    )
    public void searchGoogleImage(MessageReceivedEvent event, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("google")) {
                RestRequest request = new RestRequest();
                request.setUrl("https://www.googleapis.com/customsearch/v1");
                request.add("key", DRIVER.getProperty(DRIVER.CONFIG, "googleauthtoken", "AIzaSyBeBkYo8p8FvwweSOQcmco-jdnP6rFBPgE").toString());
                request.add("cx", DRIVER.getProperty(DRIVER.CONFIG, "googlecustomsearchid","002710779101845872719:o_wp4w-dqqi").toString());
                request.add("q", args[1].replace(",", "+"));
                request.add("searchType", "image");
                JSONObject json = request.fetchJson();
                JSONArray array = json.getJSONArray("items");
                ArrayList<String> linkcache = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    String link = ((JSONObject) array.get(i)).getString("link");
                    Console.debug(link);
                    linkcache.add(link);
                    searchmarker.put(event.getGuild(), 1);
                }
                links.put(event.getGuild(), linkcache);
                BotUtils.sendMessage(event.getChannel(), links.get(event.getGuild()).get(searchmarker.get(event.getGuild())), false);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), ShortMessageBuilder.shortMessage("Search Engine Unknown"), true);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    @Command(
            command = "next",
            description = "NextImage",
            alias = "next",
            arguments = {},
            permission = Permissions.EMBED_LINKS
    )
    public void nextImage(MessageReceivedEvent event, String[] args) {
        searchmarker.put(event.getGuild(), searchmarker.get(event.getGuild())+1);
        if (links.get(event.getGuild()).size() > searchmarker.get(event.getGuild())) {
            BotUtils.sendMessage(event.getChannel(), links.get(event.getGuild()).get(searchmarker.get(event.getGuild())), false);
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), ShortMessageBuilder.shortMessage("End reached"), true);
            links.remove(event.getGuild());
            searchmarker.remove(event.getGuild());
        }
    }
    @Command(
            command = "clear",
            description = "ClearImages",
            alias = "clear",
            arguments = {},
            permission = Permissions.EMBED_LINKS
    )
    public void clearImages(MessageReceivedEvent event, String[] args) {
        BotUtils.sendEmbMessage(event.getChannel(), ShortMessageBuilder.shortMessage("Cleared"), true);
        links.remove(event.getGuild());
        searchmarker.remove(event.getGuild());
    }
}
