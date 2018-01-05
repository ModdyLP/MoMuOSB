package modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import discord.BotUtils;
import sx.blah.discord.handle.obj.IGuild;
import util.Console;

import java.util.ArrayList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final ArrayList<AudioTrack> realqueue;
    private final IGuild guild;

    public ArrayList<AudioTrack> getQueue() {
        return realqueue;
    }


    /**
     * @param player The audio player this scheduler uses
     * @param guild
     */
    public TrackScheduler(AudioPlayer player, IGuild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
        this.realqueue = new ArrayList<>();
        this.guild = guild;
    }

    /**
     * Add the next track to queue or play right away if nothing is in the queue.
     *
     * @param track The track to play or add to queue.
     */
    public void queue(AudioTrack track) {
        // Calling startTrack with the noInterrupt set to true will start the track only if nothing is currently playing. If
        // something is playing, it returns false and does nothing. In that case the player was already playing so this
        // track goes to the queue instead.
        realqueue.add(track);
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        } else {
            BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(track.getInfo().title, String.valueOf(getQueue().size()), String.valueOf(getAudiotrackPositioninQueue(track)), MainMusic.PLAYING), MainMusic.playmessages.get(guild));
        }
        Console.debug(realqueue.size()+"   "+getAudiotrackPositioninQueue(track));
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        AudioTrack track = queue.poll();
        if (track != null) {
            player.startTrack(track, false);
            BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(track.getInfo().title, String.valueOf(getQueue().size()), String.valueOf(getAudiotrackPositioninQueue(track)), MainMusic.PLAYING), MainMusic.playmessages.get(guild));
        }
    }

    public int getAudiotrackPositioninQueue(AudioTrack track) {
        if (track != null) {
            AtomicInteger position = new AtomicInteger();
            realqueue.forEach(obj -> {
                if (obj.equals(track)) {
                    return;
                }
                position.incrementAndGet();
            });
            return position.get();
        } else {
            return -1;
        }
    }


    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        realqueue.remove(track);
        if (endReason.mayStartNext) {
            nextTrack();
        } else {
            if (queue.isEmpty() && MainMusic.playmessages.get(guild) != null) {
                BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState("Queue End", String.valueOf(this.getQueue().size()), "0", MainMusic.STOPPED), MainMusic.playmessages.get(guild));
            }
        }
    }

    @Override
    public void onPlayerPause(AudioPlayer player) {
        super.onPlayerPause(player);
        if (player.getPlayingTrack() != null) {
            BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(player.getPlayingTrack().getInfo().title, String.valueOf(getQueue().size()), String.valueOf(getAudiotrackPositioninQueue(player.getPlayingTrack())), MainMusic.PAUSED), MainMusic.playmessages.get(guild));
        }
    }

    @Override
    public void onPlayerResume(AudioPlayer player) {
        super.onPlayerResume(player);
        if (player.getPlayingTrack() != null) {
            BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(player.getPlayingTrack().getInfo().title, String.valueOf(getQueue().size()), String.valueOf(getAudiotrackPositioninQueue(player.getPlayingTrack())), MainMusic.PLAYING), MainMusic.playmessages.get(guild));
        }
    }
}
