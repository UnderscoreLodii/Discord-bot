package audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.managers.channel.concrete.VoiceChannelManager;

import java.util.HashMap;

public class GlobalAudioManager {
    private HashMap<Long, GuildMusicManager> guildMusicManagerMap = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager;
    private final VoiceConnectionHandler voiceConnectionHandler;

    public GlobalAudioManager(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        audioPlayerManager = new DefaultAudioPlayerManager();
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    public void loadAndPlay(Guild guild, String path) {
        Long guildId = guild.getIdLong();
        GuildMusicManager guildMusicManager = guildMusicManagerMap.computeIfAbsent(guildId, e -> new GuildMusicManager(guild, audioPlayerManager, voiceConnectionHandler));

        if (guild.getAudioManager().getSendingHandler() == null) {
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());
        }

        audioPlayerManager.loadItem(path, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                guildMusicManager.addTrack(track);
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                for (AudioTrack track : playlist.getTracks()) {
                    guildMusicManager.addTrack(track);
                }
            }

            @Override
            public void noMatches() {
                // Notify the user that we've got nothing
                //not used yet
            }

            @Override
            public void loadFailed(FriendlyException throwable) {
                // Notify the user that everything exploded
                //not used yet
            }
        });
    }
}
