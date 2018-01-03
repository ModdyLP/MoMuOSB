package modules.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import discord.BotUtils;
import sx.blah.discord.handle.obj.IGuild;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * This class schedules tracks for the audio player. It contains the queue of tracks.
 */
public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue;
    private final IGuild guild;

    public BlockingQueue<AudioTrack> getQueue() {
        return queue;
    }


    /**
     * @param player The audio player this scheduler uses
     * @param guild
     */
    public TrackScheduler(AudioPlayer player, IGuild guild) {
        this.player = player;
        this.queue = new LinkedBlockingQueue<>();
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
        if (!player.startTrack(track, true)) {
            queue.offer(track);
        } else {
            BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(track.getInfo().title, String.valueOf(this.getQueue().size()), String.valueOf(track.getPosition())), MainMusic.playmessages.get(guild));
        }
    }

    /**
     * Start the next track, stopping the current one if it is playing.
     */
    public void nextTrack() {
        // Start the next track, regardless of if something is already playing or not. In case queue was empty, we are
        // giving null to startTrack, which is a valid argument and will simply stop the player.
        AudioTrack track = queue.poll();
        player.startTrack(track, false);
        BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState(track.getInfo().title, String.valueOf(this.getQueue().size()), String.valueOf(track.getPosition())), MainMusic.playmessages.get(guild));
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        // Only start the next track if the end reason is suitable for it (FINISHED or LOAD_FAILED)
        if (endReason.mayStartNext) {
            nextTrack();
        } else {
            if (queue.isEmpty()) {
                BotUtils.updateEmbMessage(MainMusic.playmessages.get(guild).getChannel(), MainMusic.updateState("Queue End", String.valueOf(this.getQueue().size()), String.valueOf(0)), MainMusic.playmessages.get(guild));
            }
        }
    }
}
