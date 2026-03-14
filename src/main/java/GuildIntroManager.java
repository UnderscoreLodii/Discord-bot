import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.entities.Member;

import java.util.HashMap;
import java.util.Map;

public class GuildIntroManager {

    //HashMap mapping User's ID to String - a path to audio file
    private Map<Long, String> intros = new HashMap<>();
    private Map<Long, IntroTimer> introTimer = new HashMap<>();

    public void addIntroToGivenId(Long Id, String intro) {
        intros.put(Id, intro);
    }

    public String getIntroForGivenMember(Member member) {
        return intros.get(member.getIdLong());
    }

    public boolean handleCooldown(Member member) {
        Long memberId =  member.getIdLong();
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
