import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class SetIntroCommand implements IBotCommand{

    private VoiceJoinIntroHandler voiceJoinIntroHandler;

    SetIntroCommand(VoiceJoinIntroHandler  voiceJoinIntroHandler) {
        this.voiceJoinIntroHandler = voiceJoinIntroHandler;
    }

    @Override
    public String getName() {
        return "setintro";
    }

    @Override
    public String getDescription() {
        return "sets intro to voice channel for given user";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING,"url", "Soundcloud link to intro").setRequired(true),
                new OptionData(OptionType.USER,"target", "Target user (only for admins)"));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        String url = event.getOption("url").getAsString();
        OptionMapping targetOption = event.getOption("target");
        Member targetMember = event.getMember();

        if(targetOption!=null) {
            if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) targetMember = targetOption.getAsMember();
            else {
                event.getHook().editOriginal("❌ You don't have permission to change others' intros!").queue();
                return;
            }
        }

        try {
            voiceJoinIntroHandler.addIntroToGivenMember(targetMember, url);

            event.getHook().editOriginal("Successfully set " + targetMember.getEffectiveName() + "'s intro to: " + url).queue();

        } catch (Exception e) {
            event.getHook().editOriginal("Something went wrong while setting the intro.").queue();
            e.printStackTrace();
        }
    }
}
