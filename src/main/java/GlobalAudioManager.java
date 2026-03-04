import java.util.HashMap;

public class GlobalAudioManager {
    HashMap<Long, GuildMusicManager> guildMusicManagerMap = new HashMap<>();

    GuildMusicManager getGuildMusicManagerForGivenGuildId(Long guildId) {
        return guildMusicManagerMap.get(guildId);
    }
}
