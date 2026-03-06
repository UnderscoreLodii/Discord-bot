import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.audio.AudioSendHandler;

public class GuildMusicManager {
    private final AudioPlayer audioPlayer;
    private final TrackScheduler trackScheduler;
    private final AudioSendHandler audioSendHandler;

    public GuildMusicManager(AudioPlayerManager playerManager, Runnable onQueueEmpty) {
        audioPlayer = playerManager.createPlayer();
        trackScheduler = new TrackScheduler(audioPlayer, onQueueEmpty);
        audioPlayer.addListener(trackScheduler);
        this.audioSendHandler = new AudioPlayerSendHandler(audioPlayer);
    }

    public void addTrack(AudioTrack audioTrack) {
        if (!audioPlayer.startTrack(audioTrack, true)) {
            trackScheduler.queue(audioTrack);
        }
    }

    public AudioSendHandler getSendHandler() {
        return audioSendHandler;
    }

}
