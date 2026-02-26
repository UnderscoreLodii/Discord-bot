import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public class VoiceListener extends ListenerAdapter {

    private final VoiceJoinIntroManager voiceJoinIntroManager;

    public VoiceListener(VoiceJoinIntroManager voiceJoinIntroManager) {
        this.voiceJoinIntroManager = voiceJoinIntroManager;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        AudioChannelUnion channelJoined = event.getChannelJoined();
        if(channelJoined != null)voiceJoinIntroManager.handleMemberJoiningVoice(channelJoined, member);
    }
}
