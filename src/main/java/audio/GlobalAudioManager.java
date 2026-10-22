package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.source.soundcloud.SoundCloudAudioSourceManager;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioItem;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;

public class GlobalAudioManager {
    private static final Logger log = LoggerFactory.getLogger(GlobalAudioManager.class);

    private HashMap<Long, GuildMusicManager> guildMusicManagerMap = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager;
    private final VoiceConnectionHandler voiceConnectionHandler;

    public GlobalAudioManager(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        audioPlayerManager = new DefaultAudioPlayerManager();

        AudioSourceManagers.registerRemoteSources(
                audioPlayerManager,
                SoundCloudAudioSourceManager.class,
                com.sedmelluq.discord.lavaplayer.source.youtube.YoutubeAudioSourceManager.class
        );

        audioPlayerManager.registerSourceManager(makeYoutubeSource());

        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public CompletableFuture<AudioItem> loadItemFromUrl(Guild guild, String path){
        CompletableFuture<AudioItem> future = new CompletableFuture<>();

        audioPlayerManager.loadItem(path, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                future.complete(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                future.complete(playlist);
            }

            @Override
            public void noMatches() {
                future.complete(null);
                log.warn("No matches found for url: {}", path);
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                future.complete(null);
                log.warn("Failed to load item from url: {}", throwable.getMessage());
            }
        });

        return future;
    }

    public void playLoadedIem(Guild guild, AudioItem item){
        GuildMusicManager guildMusicManager = getGuildMusicManager(guild);
        if (item instanceof AudioPlaylist){
            for (AudioTrack track : ((AudioPlaylist) item).getTracks()) {
                guildMusicManager.addTrack(track);
            }
        }
        else if (item instanceof AudioTrack){
            guildMusicManager.addTrack((AudioTrack) item);
        }
        else {
            log.warn("Tried to play invalid item");
        }
    }

    private GuildMusicManager getGuildMusicManager(Guild guild) {
        Long guildId = guild.getIdLong();
        GuildMusicManager guildMusicManager = guildMusicManagerMap.computeIfAbsent(guildId, e -> new GuildMusicManager(guild, audioPlayerManager, voiceConnectionHandler));
        if (guild.getAudioManager().getSendingHandler() == null) {
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
        }
        return guildMusicManager;
    }

    private YoutubeAudioSourceManager makeYoutubeSource(){
        YoutubeAudioSourceManager youtubeSource = new YoutubeAudioSourceManager();
        return youtubeSource;
    }
}
