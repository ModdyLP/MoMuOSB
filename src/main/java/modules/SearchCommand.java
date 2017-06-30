package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import main.Prefix;
import org.json.JSONArray;
import org.json.JSONObject;
import storage.RestRequest;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.EmbedBuilder;
import util.Console;
import util.SMB;
import util.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by ModdyLP on 29.06.2017. Website: https://moddylp.de/
 */
public class SearchCommand extends Module{

    HashMap<IGuild, ArrayList<String>> links = new HashMap<>();
    HashMap<IGuild, Integer> searchmarker = new HashMap<>();
    HashMap<IGuild, String> search = new HashMap<>();


    @Command(
            command = "search",
            description = "Search a Image",
            alias = "sq",
            arguments = {"SearchEngine(Google)","Query []"},
            permission = Permissions.EMBED_LINKS,
            prefix = Prefix.GAME_PREFIX
    )
    public void searchGoogleImage(MessageReceivedEvent event, String[] args) {
        new Thread(() -> {
            try {
                links.remove(event.getGuild());
                searchmarker.remove(event.getGuild());
                search.remove(event.getGuild());
                if (args[0].equalsIgnoreCase("google")) {
                    if (DRIVER.getPropertyOnly(DRIVER.CONFIG, "googleauthtoken").equals("")) {
                        BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.ERROR+LANG.getTranslation("searchtoken_google")), true);
                        return;
                    }
                    RestRequest request = new RestRequest();
                    request.setUrl("https://www.googleapis.com/customsearch/v1");
                    request.add("key", DRIVER.getPropertyOnly(DRIVER.CONFIG, "googleauthtoken").toString());
                    request.add("cx", DRIVER.getPropertyOnly(DRIVER.CONFIG, "googlecustomsearchid").toString());
                    request.add("q", Utils.makeArgsToString(args, new String[] {args[0]}).replace(" ", "+"));
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
                    search.put(event.getGuild(), Utils.makeArgsToString(args, new String[] {args[0]}));
                    sendMessage(event);
                } else {
                    BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("engine_unknown")), true);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();

    }
    @Command(
            command = "next",
            description = "NextImage",
            alias = "next",
            arguments = {},
            permission = Permissions.EMBED_LINKS,
            prefix = Prefix.GAME_PREFIX
    )
    public void nextImage(MessageReceivedEvent event, String[] args) {
        if (searchmarker.get(event.getGuild()) != null) {
            searchmarker.put(event.getGuild(), searchmarker.get(event.getGuild()) + 1);
            if (links.get(event.getGuild()).size() > searchmarker.get(event.getGuild())) {
                sendMessage(event);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("results_end")), true);
                links.remove(event.getGuild());
                searchmarker.remove(event.getGuild());
                search.remove(event.getGuild());
            }
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("no_search")), true);
        }
    }
    @Command(
            command = "clear",
            description = "ClearImages",
            alias = "clear",
            arguments = {},
            permission = Permissions.EMBED_LINKS,
            prefix = Prefix.GAME_PREFIX

    )
    public void clearImages(MessageReceivedEvent event, String[] args) {
        if (searchmarker.get(event.getGuild()) != null) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("results_cleared")), true);
            links.remove(event.getGuild());
            searchmarker.remove(event.getGuild());
            search.remove(event.getGuild());
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("no_search")), true);
        }
    }

    @Command(
            command = "getImage",
            description = "GetImage",
            alias = "getI",
            arguments = {"Number"},
            permission = Permissions.EMBED_LINKS,
            prefix = Prefix.GAME_PREFIX

    )
    public void getImage(MessageReceivedEvent event, String[] args) {
        if (searchmarker.get(event.getGuild()) != null) {
            searchmarker.put(event.getGuild(), Integer.parseInt(args[0]));
            if (searchmarker.get(event.getGuild()) > 0 && links.get(event.getGuild()).size() > searchmarker.get(event.getGuild())) {
                sendMessage(event);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("result_out")), true);
            }
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("no_search")), true);
        }
    }

    private void sendMessage(MessageReceivedEvent event) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle("Your search Result");
        builder.withDescription("Search: "+search.get(event.getGuild())
                +"\nResult "+searchmarker.get(event.getGuild())+" of "+links.get(event.getGuild()).size());
        builder.withColor(Color.cyan);
        builder.withImage(links.get(event.getGuild()).get(searchmarker.get(event.getGuild())));
        builder.withUrl(links.get(event.getGuild()).get(searchmarker.get(event.getGuild())));
        BotUtils.sendEmbMessage(event.getChannel(), builder, false);
    }
}
