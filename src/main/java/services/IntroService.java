package services;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import repositories.IntroDataRepository;
import util.CooldownHandler;

public class IntroService {

    private final MusicService musicService;
    private final CooldownHandler cooldownHandler;
    private final IntroDataRepository introDataRepository;

    public IntroService(MusicService musicService, IntroDataRepository introDataRepository) {
        this.musicService = musicService;
        this.introDataRepository = introDataRepository;
        cooldownHandler = new CooldownHandler(60000L);
    }

    public void addIntroToGivenMember(Member member, String intro) {
        if (member == null) return;
        introDataRepository.addIntro(member.getGuild().getIdLong(), member.getUser().getIdLong(), intro);
    }

    public void deleteIntroFromGivenMember(Member member) {
        if (member == null) return;
        introDataRepository.deleteIntro(member.getGuild().getIdLong(), member.getUser().getIdLong());
    }

    public void handleMemberJoiningVoice(AudioChannelUnion channelJoined, Member member) {
        Long guildId = member.getGuild().getIdLong();
        Long userId = member.getUser().getIdLong();
        String intro = introDataRepository.getIntro(guildId, userId);
        if(intro != null && cooldownHandler.handleCooldown(guildId, userId)) {
            musicService.connectAndPlay(channelJoined, intro);
        }
    }
}
