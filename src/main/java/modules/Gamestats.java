package modules;

import discord.BotUtils;
import events.Command;
import events.Module;
import org.json.JSONObject;
import storage.LanguageInterface;
import storage.LanguageMethod;
import storage.RestRequest;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.util.EmbedBuilder;
import util.Console;
import util.Fast;
import util.SMB;
import util.Utils;

import java.awt.*;

/**
 * Created by N.Hartmann on 04.07.2017.
 * Copyright 2017
 */
public class Gamestats extends Module implements Fast{
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
                builder.withTitle(args[1]+" "+LANG.getTranslation("game_overwatch_level")+": "+(competitive.getInt("prestige") * 100 + competitive.getInt("level")));
                builder.appendField(LANG.getTranslation("game_overwatch_comprank")+": ", competitive.get("comprank").toString()+"   "+competitive.getString("tier"), false);
                builder.appendField(LANG.getTranslation("game_overwatch_gameswon"), quickplay.get("games_won").toString(), false);
                IMessage message = BotUtils.sendEmbMessage(event.getChannel(), builder, false);
                BotUtils.addReactionToMessage(message, "\u274C");
            } else {
                BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("game_unsupported")), true);
            }
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("commonmessage_error"), ex.getMessage())), true);
            Console.error(ex);
        }
    }

    @LanguageMethod(
            languagestringcount = 4
    )
    @Override
    public void setdefaultLanguage() {
        DRIVER.setProperty(DEF_LANG, "game_overwatch_level", "Level");
        DRIVER.setProperty(DEF_LANG, "game_overwatch_comprank", "Competitive Rank");
        DRIVER.setProperty(DEF_LANG, "game_overwatch_gameswon", "Games Won Quickplay");
        DRIVER.setProperty(DEF_LANG, "game_unsupported", "This Game isn't supported.");
    }
}
