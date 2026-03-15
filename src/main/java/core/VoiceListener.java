package core;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent;
import services.IntroService;

public class VoiceListener extends ListenerAdapter {

    private final IntroService introService;

    public VoiceListener(IntroService introService) {
        this.introService = introService;
    }

    @Override
    public void onGuildVoiceUpdate(GuildVoiceUpdateEvent event) {
        Member member = event.getMember();
        if(member.getUser().isBot()) return;
        AudioChannelUnion channelJoined = event.getChannelJoined();
        if(channelJoined != null)introService.handleMemberJoiningVoice(channelJoined, member);
    }
}
