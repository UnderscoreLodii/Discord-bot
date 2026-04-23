package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import services.IntroService;

import java.util.List;

public class DeleteIntroCommand implements IBotCommand {

    private static final Logger log = LoggerFactory.getLogger(DeleteIntroCommand.class);
    private IntroService introService;

    public DeleteIntroCommand(IntroService introService) {
        this.introService = introService;
    }

    @Override
    public DefaultMemberPermissions getPermissions() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public String getName() {
        return "deleteintro";
    }

    @Override
    public String getDescription() {
        return "deletes the intro for you or given user";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.USER,"target", "Target user (only for admins)"));
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        OptionMapping targetOption = event.getOption("target");
        Member targetMember = event.getMember();

        if(targetOption!=null) {
            if(event.getMember().hasPermission(Permission.ADMINISTRATOR)) targetMember = targetOption.getAsMember();
            else {
                event.getHook().editOriginal("❌ You don't have permission to change others' intros!").queue();
                return;
            }
        }
        if (targetMember == null) {
            event.getHook().editOriginal("❌ Could not find that user in this server.").queue();
            return;
        }

        try {
            introService.deleteIntroFromGivenMember(targetMember);

            event.getHook().editOriginal("Successfully deleted " + targetMember.getEffectiveName() + "'s intro").queue();

        } catch (Exception e) {
            event.getHook().editOriginal("Something went wrong while deleting an intro.").queue();
            log.error("Failed to delete intro for user {}", targetMember.getId(), e);
        }
    }
}
