import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEvent;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.player.event.TrackEndEvent;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer audioPlayer;
    private LinkedBlockingQueue<AudioTrack> queue;
    private Runnable onQueueEmpty;

    public TrackScheduler(AudioPlayer audioPlayer, Runnable onQueueEmpty) {
        this.audioPlayer = audioPlayer;
        this.onQueueEmpty = onQueueEmpty;
        this.queue = new LinkedBlockingQueue<>();
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        super.onTrackEnd(player, track, endReason);
        if(!nextTrack()) onQueueEmpty.run();
    }

    public void queue(AudioTrack audioTrack) {
        queue.offer(audioTrack);
    }

    public boolean nextTrack(){
        AudioTrack track = queue.poll();
        if(track == null) return false;
        else {
            audioPlayer.startTrack(track, false);
            return true;
        }
    }
}
