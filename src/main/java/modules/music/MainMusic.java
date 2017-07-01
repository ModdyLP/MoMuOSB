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
import util.Prefix;
import sx.blah.discord.handle.impl.events.guild.channel.message.MessageReceivedEvent;
import sx.blah.discord.handle.obj.IChannel;
import sx.blah.discord.handle.obj.IGuild;
import sx.blah.discord.handle.obj.IVoiceChannel;
import sx.blah.discord.handle.obj.Permissions;
import sx.blah.discord.util.audio.AudioPlayer;
import util.SMB;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ModdyLP on 30.06.2017. Website: https://moddylp.de/
 */
public class MainMusic extends Module{

    public static final AudioPlayerManager playerManager = new DefaultAudioPlayerManager();
    private static final Map<Long, GuildMusicManager> musicManagers  = new HashMap<>();

    @Command(
            command = "join",
            alias = "join",
            description = "The Bot joins voice Server",
            arguments = {},
            permission = "music_control",
            prefix = Prefix.MUSIC_PREFIX
    )
    public boolean joinCommand(MessageReceivedEvent event, String[] args) {
        IVoiceChannel userVoiceChannel = event.getAuthor().getVoiceStateForGuild(event.getGuild()).getChannel();

        if(userVoiceChannel == null)
            return false;

        userVoiceChannel.join();
        this.volumeCommand(event, new String[] {DRIVER.getPropertyOnly(DRIVER.CONFIG, "defaultvolume").toString()});
        return true;
    }

    @Command(
            command = "leave",
            alias = "leave",
            description = "The Bot leaves voice Server",
            arguments = {},
            permission = "music_control",
            prefix = Prefix.MUSIC_PREFIX
    )
    public boolean leaveCommand(MessageReceivedEvent event, String[] args) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if(botVoiceChannel == null)
            return false;

        AudioPlayer audioP = AudioPlayer.getAudioPlayerForGuild(event.getGuild());

        audioP.clear();

        botVoiceChannel.leave();
        return true;
    }

    @Command(
            command = "play",
            alias = "play",
            description = "The Bot plays the first song.",
            arguments = {"Url or Path"},
            permission = "music_control",
            prefix = Prefix.MUSIC_PREFIX
    )
    public boolean playCommand(MessageReceivedEvent event, String[] args) {
        IVoiceChannel botVoiceChannel = event.getClient().getOurUser().getVoiceStateForGuild(event.getGuild()).getChannel();

        if(botVoiceChannel == null) {
            BotUtils.sendEmbMessage(event.getChannel(), SMB.shortMessage(LANG.getTranslation("music_notinchannel")), true);
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
            prefix = Prefix.MUSIC_PREFIX
    )
    public boolean skipCommand(MessageReceivedEvent event, String[] args) {
        skipTrack(event.getChannel());
        return true;
    }

    @Command(
            command = "volume",
            alias = "vol",
            description = "The Bot volume.",
            arguments = {"Volume"},
            permission = "music_control",
            prefix = Prefix.MUSIC_PREFIX
    )
    public boolean volumeCommand(MessageReceivedEvent event, String[] args) {
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
        GuildMusicManager musicManager = musicManagers.computeIfAbsent(guildId, k -> new GuildMusicManager(playerManager));

        guild.getAudioManager().setAudioProvider(musicManager.getAudioProvider());

        return musicManager;
    }

    private static void loadAndPlay(final IChannel channel, final String trackUrl) {
        GuildMusicManager musicManager = getGuildAudioPlayer(channel.getGuild());

        playerManager.loadItemOrdered(musicManager, trackUrl, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(String.format(LANG.getTranslation("music_add"), track.getInfo().title)), true);

                play(musicManager, track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                AudioTrack firstTrack = playlist.getSelectedTrack();

                if (firstTrack == null) {
                    firstTrack = playlist.getTracks().get(0);
                }

                BotUtils.sendEmbMessage(channel, SMB.shortMessage(String.format(LANG.getTranslation("music_add_queue"),firstTrack.getInfo().title,playlist.getName())), true);

                play(musicManager, firstTrack);
            }

            @Override
            public void noMatches() {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(String.format(LANG.getTranslation("music_notfound"), trackUrl)), true);
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                BotUtils.sendEmbMessage(channel, SMB.shortMessage(LANG.getTranslation("music_notloaded")+exception.getMessage()), true);
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
}
