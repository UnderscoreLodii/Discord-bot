import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;

import java.util.HashMap;
import java.util.Map;

public class VoiceJoinIntroManager {

    private final VoiceConnectionHandler voiceConnectionHandler;
    //HashMap mapping User's ID to String - a path to audio file
    private Map<Long, String> intros = new HashMap<>();
    private Map<Long, IntroTimer> introTimer = new HashMap<>();

    public VoiceJoinIntroManager(VoiceConnectionHandler voiceConnectionHandler) {
        this.voiceConnectionHandler = voiceConnectionHandler;
        //temporary hard coded thing
        //testing
        //addIntroToGivenID(Long.parseLong(Dotenv.load().get("GALBAN_ID")), "sexyback.mp3");
        addIntroToGivenID(Long.parseLong(Dotenv.load().get("TESTING_ID")), "sexyback.mp3");
    }

    public void addIntroToGivenMember(Member member, String intro) {
        intros.put(member.getIdLong(), intro);
    }
    public void addIntroToGivenID(Long id, String intro) {
        intros.put(id, intro);
    }

    public void handleMemberJoiningVoice(AudioChannelUnion channelJoined, Member member) {
        Long  memberId = member.getIdLong();
        if(handleCooldown(memberId)) voiceConnectionHandler.connectToChannelAndPlayAudio(channelJoined, intros.get(member.getIdLong()));
    }

    private boolean handleCooldown(Long memberId) {
        IntroTimer timer = introTimer.get(memberId);
        if (timer == null){
            introTimer.put(memberId, IntroTimer.buildTheTimerAndStart(60000));
        }
        else if (timer.isReady()){
            timer.startTimer(60000);
        }
        else return false;
        return true;
    }
}
