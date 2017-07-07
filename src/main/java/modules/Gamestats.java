package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import org.json.JSONObject;
import storage.LanguageInterface;
import storage.RestRequest;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.util.EmbedBuilder;
import util.Console;
import util.SMB;
import util.Utils;

import java.awt.*;

/**
 * Created by N.Hartmann on 04.07.2017.
 * Copyright 2017
 */
public class Gamestats extends Module implements LanguageInterface{
    @Command(
            command = "gamestats",
            alias = "gstats",
            arguments = {"Game", "Username"},
            description = "Get Statistics about a Player in a Game",
            permission = "gamestats",
            prefix = "~"
    )
    public void getGameStats(MessageReceivedEvent event, String[] args) {
        try {
            if (args[0].equalsIgnoreCase("overwatch") || args[0].equalsIgnoreCase("OW")) {
                RestRequest request = new RestRequest();
                request.setUrl("https://owapi.net/api/v3/u/" + args[1].replace("#", "-") + "/stats");
                request.add("format", "json_pretty");
                JSONObject json = request.fetchJson();
                EmbedBuilder builder = new EmbedBuilder();
                builder.withColor(Color.green);
                JSONObject stats = json.getJSONObject("eu").getJSONObject("stats");
                JSONObject competitive = stats.getJSONObject("competitive").getJSONObject("overall_stats");
                JSONObject quickplay = stats.getJSONObject("quickplay").getJSONObject("game_stats");
                builder.withThumbnail(competitive.getString("avatar"));
                builder.withTitle(args[1]+"  Level: "+(competitive.getInt("prestige") * 100 + competitive.getInt("level")));
                builder.appendField("Competitive Rank", competitive.get("comprank").toString()+"   "+competitive.getString("tier"), false);
                builder.appendField("Games Won Quickplay", quickplay.get("games_won").toString(), false);
                BotUtils.sendEmbMessage(event.getChannel(), builder, false);
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage("This Game isn't supported."), true);
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
            ex.printStackTrace();
        }
    }

    @Override
    public void setdefaultLanguage() {

    }
}
