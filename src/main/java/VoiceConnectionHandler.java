import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public class VoiceConnectionHandler {

    private final GlobalAudioManager globalAudioManager;

    VoiceConnectionHandler(GlobalAudioManager audioPlayerManager) {
        this.globalAudioManager = audioPlayerManager;
    }

    public void connectToChannelAndPlayAudio(AudioChannelUnion channel, String audioPath) {
        GuildMusicManager gmm = globalAudioManager.getGuildMusicManagerForGivenGuildId(channel.getGuild().getIdLong());
    }
}
