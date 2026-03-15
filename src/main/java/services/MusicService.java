package services;

import audio.GlobalAudioManager;
import audio.VoiceConnectionHandler;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

public class MusicService {
    private final VoiceConnectionHandler voiceConnectionHandler;
    private final GlobalAudioManager globalAudioManager;

    public MusicService(VoiceConnectionHandler voiceConnectionHandler, GlobalAudioManager globalAudioManager) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        this.globalAudioManager = globalAudioManager;
    }

    public void connectAndPlay(AudioChannelUnion channel, String trackUrl){
        voiceConnectionHandler.joinChannel(channel);
        globalAudioManager.loadAndPlay(channel.getGuild(), trackUrl);
    }
}
