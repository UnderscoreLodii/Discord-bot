import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class VoiceJoinIntroHandler {

    private final VoiceConnectionHandler voiceConnectionHandler;
    private final Map<Long, GuildIntroManager> guildIntroManagers = new HashMap<>();

    public VoiceJoinIntroHandler(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        //temporary hard coded thing
        guildIntroManagers.put(Long.parseLong(Dotenv.load().get("HYPIXELFF_ID")), new GuildIntroManager());
        guildIntroManagers.put(Long.parseLong(Dotenv.load().get("TEST_SERVER_ID")), new GuildIntroManager());
    }

    public void addIntroToGivenMember(Member member, String intro) {
        guildIntroManagers.computeIfAbsent(member.getGuild().getIdLong(), e -> new GuildIntroManager()).addIntroToGivenId(member.getIdLong(), intro);
    }

    public void handleMemberJoiningVoice(AudioChannelUnion channelJoined, Member member) {
        Long  guildId = member.getGuild().getIdLong();
        GuildIntroManager guildIntroManager = guildIntroManagers.get(guildId);
        String intro = guildIntroManager.getIntroForGivenMember(member);
        if(intro != null && guildIntroManager.handleCooldown(member)) voiceConnectionHandler.connectToChannelAndPlayAudio(channelJoined, intro);
    }


}
