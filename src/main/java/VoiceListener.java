import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;

public class VoiceListener extends ListenerAdapter {

    private final VoiceConnectionHandler voiceConnectionHandler;
    private GalbanTimer galbanTimer;

    public VoiceListener(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getEntity();
        AudioChannelUnion channelJoined = event.getChannelJoined();

        if(galbanTimer.isReady() && member.getId().equals(Dotenv.load().get("ID_GALBAN"))) {
            handleGalbanJoin(event, channelJoined);
        }
    }

    private void handleGalbanJoin(GuildVoiceUpdateEvent event, AudioChannelUnion channelJoined) {
        galbanTimer.galbanJoined();
    }
}
