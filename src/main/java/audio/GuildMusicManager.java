package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;
import net.dv8tion.jda.api.audio.AudioSendHandler;
import net.dv8tion.jda.api.entities.Guild;

import java.util.concurrent.LinkedBlockingQueue;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioSendHandler audioSendHandler;
    private final VoiceConnectionHandler voiceConnectionHandler;
    private final Guild guild;

    public GuildMusicManager(Guild guild, AudioPlayerManager playerManager, VoiceConnectionHandler voiceConnectionHandler) {
        this.guild = guild;
        this.voiceConnectionHandler = voiceConnectionHandler;
        audioPlayer = playerManager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer);
        audioPlayer.addListener(trackScheduler);
        this.audioSendHandler = new AudioPlayerSendHandler(audioPlayer);
    }
    public AudioSendHandler getSendHandler() {
        return audioSendHandler;
    }

    public void addTrack(AudioTrack audioTrack) {
        if (!audioPlayer.startTrack(audioTrack, true)) {
            trackScheduler.queue(audioTrack);
        }
    }

    private void onQueueEnd(){
        voiceConnectionHandler.leaveChannel(guild);
    }

    private class TrackScheduler extends AudioEventAdapter {
        private final AudioPlayer audioPlayer;
        private LinkedBlockingQueue<AudioTrack> queue =  new LinkedBlockingQueue<>();

        public TrackScheduler(AudioPlayer audioPlayer) {
            this.audioPlayer = audioPlayer;
        }

        @Override
        public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
            super.onTrackEnd(player, track, endReason);
            if(!nextTrack()) GuildMusicManager.this.onQueueEnd();
        }

        private void queue(AudioTrack audioTrack) {
            queue.offer(audioTrack);
        }

        private boolean nextTrack(){
            AudioTrack track = queue.poll();
            if(track == null) return false;
            else {
                audioPlayer.startTrack(track, false);
                return true;
            }
        }
    }
}
