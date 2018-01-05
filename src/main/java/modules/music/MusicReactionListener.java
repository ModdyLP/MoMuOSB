package modules.music;

import discord.BotUtils;
import sx.blah.discord.api.events.EventSubscriber;
import sx.blah.discord.handle.impl.events.guild.channel.message.reaction.ReactionAddEvent;
import sx.blah.discord.handle.impl.obj.ReactionEmoji;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import util.Console;

import static util.Fast.INIT;

public class MusicReactionListener {

    private static MusicReactionListener instance;
    public static MusicReactionListener getInstance() {

        if (instance == null) {
            instance = new MusicReactionListener();
        }
        return instance;
    }



    @EventSubscriber
    public void onReactionAddEvent(ReactionAddEvent event) {
        IMessage message = event.getMessage();
        if (MainMusic.playmessages.containsValue(message)) {
                if (event.getReaction().getUsers().size() > 1 && event.getReaction().getUserReacted(INIT.BOT.getOurUser())) {
                    if (event.getReaction().getEmoji().equals(ReactionEmoji.of("\u23F9"))) {
                        MainMusic.stop(event.getGuild());
                    }
                    if (event.getReaction().getEmoji().equals(ReactionEmoji.of("\u23ED"))) {
                        MainMusic.skipTrack(event.getChannel());
                    }
                    if (event.getReaction().getEmoji().equals(ReactionEmoji.of("\u23EF"))) {
                        MainMusic.pause(event.getGuild());
                    }
                    if (event.getReaction().getEmoji().equals(ReactionEmoji.of("\u26D4"))) {
                        MainMusic.getInstance().leave(event.getGuild(), event.getChannel());
                    }
                    event.getReaction().getUsers().forEach(user -> {
                        if (!user.equals(INIT.BOT.getOurUser())) {
                            message.removeReaction(user, event.getReaction());
                        }
                    });
                }

        }
    }

    public void initMessage(IGuild guild) {
        if (MainMusic.playmessages.get(guild) != null) {
            BotUtils.addReactionToMessage(MainMusic.playmessages.get(guild), "\u23F9"); //STOP
            BotUtils.addReactionToMessage(MainMusic.playmessages.get(guild), "\u23EF"); //PAUSE
            BotUtils.addReactionToMessage(MainMusic.playmessages.get(guild), "\u23ED"); //NEXT
            BotUtils.addReactionToMessage(MainMusic.playmessages.get(guild), "\u26D4"); //LEAVE
        } else {
            Console.error("The message was null on reaction init.");
        }
    }
}
