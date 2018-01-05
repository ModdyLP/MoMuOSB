package modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import discord.BotUtils;
import events.Command;
import events.Module;
import storage.LanguageMethod;
import sx.blah.discord.api.internal.json.objects.EmbedObject;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IMessage;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.util.EmbedBuilder;
import sx.blah.discord.util.audio.AudioPlayer;
import util.Console;
import util.Fast;
import util.Globals;
import util.SMB;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ModdyLP on 30.06.2017. Website: https://moddylp.de/
 */
public class MainMusic extends Module implements Fast{

    public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers = new HashMap<>();
    public static final HashMap<IGuild, IMessage> playmessages = new HashMap<>();


    @Command(
            command = "join",
            alias = "join",
            description = "The Bot joins voice Server",
            arguments = {},
            permission = "music_control",
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean joinCommand(MessageReceivedEvent event, String[] args) {
        if (DRIVER.getPropertyOnly(DRIVER.CONFIG, SERVER_CONTROL.MUSIC_MODULE+"_enabled_default").equals(false) || !SERVER_CONTROL.getEnabledList(SERVER_CONTROL.MUSIC_MODULE).contains(event.getGuild().getStringID())) {
            IVoiceChannel userVoiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

            if (userVoiceChannel == null)
                return false;
            IMessage message = BotUtils.sendEmbMessage(event.getChannel(), updateState("None", "0", "0"), false);
            playmessages.put(event.getGuild(), message);
            userVoiceChannel.join();
            this.volumeMusic(event, new String[]{DRIVER.getPropertyOnly(DRIVER.CONFIG, "defaultvolume").toString()});
            if (args.length > 0) {
                playMusic(event, args);
            }
            return true;
        } else {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("disabledserver")), true);
            return false;
        }
    }

    @Command(
            command = "leave",
            alias = "leave",
            description = "The Bot leaves voice Server",
            arguments = {},
            permission = "music_control",
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean leaveMusic(MessageReceivedEvent event, String[] args) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();
        if (playmessages.containsKey(event.getGuild())) {
            BotUtils.deleteMessageOne(playmessages.get(event.getGuild()));
            playmessages.remove(event.getGuild());
        }
        if (botVoiceChannel == null) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("music_notinchannel_user")), true);
            return false;
        }

        AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());
        if (audioP != null) {
            audioP.clear();
        }
        botVoiceChannel.leave();
        return true;
    }

    @Command(
            command = "play",
            alias = "play",
            description = "The Bot plays the first song.",
            arguments = {"Url or Path"},
            permission = "music_control",
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean playMusic(MessageReceivedEvent event, String[] args) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if (botVoiceChannel == null) {
            joinCommand(event, args);
            return false;
        }

        // Turn the args back into a string separated by space
        String searchStr = args[0];

        loadAndPlay(event.getChannel(), searchStr);
        return true;
    }

    @Command(
            command = "skip",
            alias = "skip",
            description = "The Bot skips a song.",
            arguments = {},
            permission = "music_control",
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean skipTrack(MessageReceivedEvent event, String[] args) {
        skipTrack(event.getChannel());
        return true;
    }

    @Command(
            command = "volume",
            alias = "vol",
            description = "The Bot volume.",
            arguments = {"Volume"},
            permission = "music_control",
            prefix = Globals.MUSIC_PREFIX
    )
    public boolean volumeMusic(MessageReceivedEvent event, String[] args) {
        try {
            int volumebefore = getGuildAudioPlayer(event.getGuild()).player.getVolume();
            getGuildAudioPlayer(event.getGuild()).player.setVolume(Integer.parseInt(args[0]));
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(String.format(LANG.getTranslation("music_volumechange"), volumebefore, args[0])), true);
        } catch (Exception ex) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("music_volumechangeerror")), true);
        }
        return true;
    }


    private static synchronized GuildMusicManager getGuildAudioPlayer(IGuild guild) {
        long guildId = guild.getLongID();
        GuildMusicManager musicManager = musicManagers.computeIfAbsent(guildId, k -> new GuildMusicManager(playerManager, guild));

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    private static void loadAndPlay(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());


        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(String.format(LANG.getTranslation("music_add"), track.getInfo().title)), true);
                Console.debug("Loaded track: "+track.getInfo().title);
                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    musicManager.scheduler.queue(track);
                }
            }

            @Override
            public void noMatches() {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(String.format(LANG.getTranslation("music_notfound"), trackUrl)), true);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(LANG.getTranslation("music_notloaded") + exception.getMessage()), true);
            }
        });
    }

    private static void play(GuildMusicManager musicManager, AudioTrack track) {
        musicManager.scheduler.queue(track);
    }

    private static void skipTrack(IChannel channel) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());
        musicManager.scheduler.nextTrack();

        BotUtils.sendEmbMessage(channel, SMB.shortMessage(LANG.getTranslation("music_skip")), true);
    }

    @LanguageMethod(
            languagestringcount = 10
    )
    @Override
    public void setdefaultLanguage() {
        //Music
        DRIVER.setProperty(DEF_LANG, "music_notinchannel", "The Bot is not in a voice Channel.");
        DRIVER.setProperty(DEF_LANG, "music_notinchannel_user", "You are not in a Voice Channel.");
        DRIVER.setProperty(DEF_LANG, "music_volumechange", "Volume changed from %1s to %2s.");
        DRIVER.setProperty(DEF_LANG, "music_volumechangeerror", "Can't change volume.");
        DRIVER.setProperty(DEF_LANG, "music_add","Adding to queue: %1s.");
        DRIVER.setProperty(DEF_LANG, "music_add_queue", "Adding to queue %1s (first track of playlist %2s).");
        DRIVER.setProperty(DEF_LANG, "music_notfound", "Nothing found by %1s.");
        DRIVER.setProperty(DEF_LANG, "music_notloaded", "Could not play the choosen song.");
        DRIVER.setProperty(DEF_LANG, "music_skip", "Skipped to next track.");
        DRIVER.setProperty(DEF_LANG, "disabledserver", "This server is disabled for using the Music Module");

    }
    public static EmbedBuilder updateState(String song, String queuesize, String quequepos) {
        EmbedBuilder builder = new EmbedBuilder();
        builder.withTitle("Music Box");
        builder.withColor(Color.cyan);
        builder.appendField("Song", song, false);
        builder.appendField("Queue", quequepos+" /  "+queuesize, false);
        return builder;
    }
}
