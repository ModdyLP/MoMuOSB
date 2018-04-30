package discord.bot;

import main.MoMuOSBMain;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.events.ReadyEvent;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;
import storage.api.Storage;

import java.util.HashMap;

public class BotMainEvents extends ListenerAdapter implements Storage {

    private static HashMap<Integer, Boolean> ready = new HashMap<>();


    BotMainEvents() {
        for (int i = 0; i < ((double) CONFIG.get( "bot.Shards", 3)); i++)
        {
           ready.put(i, false);
        }
    }

    @Override
    public void onReady(ReadyEvent event) {
        MoMuOSBMain.logger.info("Bot is ready..."+(event.getJDA().getShardInfo().getShardId()+1)+ " "+event.getJDA().getShardInfo().getShardTotal());
        ready.put(event.getJDA().getShardInfo().getShardId(), true);
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (ready.get(event.getJDA().getShardInfo().getShardId())) {
            if (event.isFromType(ChannelType.TEXT)) {
                MoMuOSBMain.logger.info(String.format("[%s][%s] %s: %s\n", event.getGuild().getName(),
                        event.getTextChannel().getName(), event.getMember().getEffectiveName(),
                        event.getMessage().getContentRaw()));
            }
        }
    }
}
