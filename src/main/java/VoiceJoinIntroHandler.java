import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class VoiceJoinIntroHandler {

    private final VoiceConnectionHandler voiceConnectionHandler;
    private final Map<Long, GuildIntroManager> guildIntroManagers = new HashMap<>();
    private final IntroDataManager introDataManager;

    public VoiceJoinIntroHandler(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        introDataManager = new IntroDataManager();
        loadIntros();
        //temporary hard coded thing
        guildIntroManagers.computeIfAbsent(Long.parseLong(Dotenv.load().get("HYPIXELFF_ID")), e -> new GuildIntroManager()).
                addIntroToGivenId(Long.parseLong(Dotenv.load().get("GALBAN_ID")), "sexyback.mp3");
    }

    private void loadIntros(){
        for(Long guildId : introDataManager.getGuilds()){
            GuildIntroManager guildIntroManager = guildIntroManagers.computeIfAbsent(guildId, e -> new GuildIntroManager());
            for(var entry : introDataManager.getIntrosForGivenGuildId(guildId).entrySet()){
                guildIntroManager.addIntroToGivenId(entry.getKey(), entry.getValue());
            }
        }
    }

    public void addIntroToGivenMember(Member member, String intro) {
        Long guildId = member.getGuild().getIdLong();
        Long userId = member.getUser().getIdLong();
        guildIntroManagers.computeIfAbsent(guildId, e -> new GuildIntroManager()).addIntroToGivenId(userId, intro);
        introDataManager.addIntro(guildId, userId, intro);
    }

    public void handleMemberJoiningVoice(AudioChannelUnion channelJoined, Member member) {
        Long  guildId = member.getGuild().getIdLong();
        GuildIntroManager guildIntroManager = guildIntroManagers.get(guildId);
        String intro = guildIntroManager.getIntroForGivenMember(member);
        if(intro != null && guildIntroManager.handleCooldown(member)) voiceConnectionHandler.connectToChannelAndPlayAudio(channelJoined, intro);
    }


}
