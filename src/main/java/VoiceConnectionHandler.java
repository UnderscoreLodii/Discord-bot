import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceConnectionHandler {

    private final GlobalAudioManager globalAudioManager;

    VoiceConnectionHandler(GlobalAudioManager audioPlayerManager) {
        this.globalAudioManager = audioPlayerManager;
    }

    public void connectToChannelAndPlayAudio(AudioChannelUnion channel, String audioPath) {
        Guild guild = channel.getGuild();
        joinChannel(guild, channel);
        globalAudioManager.loadAndPlay(guild, audioPath, () -> this.leaveChannel(guild));
    }

    private boolean joinChannel(Guild guild, AudioChannelUnion channel){
        AudioManager manager = guild.getAudioManager();
        if (!manager.isConnected()){
            manager.openAudioConnection(channel);
            return true;
        }
        else return false;
    }

    private void leaveChannel(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
    }
}
