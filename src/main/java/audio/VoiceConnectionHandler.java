package audio;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;

public class VoiceConnectionHandler {

    public void joinChannel(AudioChannelUnion channel){
        Guild guild = channel.getGuild();
        AudioManager manager = guild.getAudioManager();
        if (!manager.isConnected()) manager.openAudioConnection(channel);
    }

    public void leaveChannel(Guild guild) {
        guild.getAudioManager().closeAudioConnection();
    }
}
