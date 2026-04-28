package commands;

import calendar.services.CalendarBirthdayService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public class DeleteBirthdayCommand implements IBotCommand{
    private final CalendarBirthdayService calendarBirthdayService;

    public DeleteBirthdayCommand(CalendarBirthdayService calendarBirthdayService){
        this.calendarBirthdayService = calendarBirthdayService;
    }

    @Override
    public DefaultMemberPermissions getPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public String getName() {
        return "deletebirthday";
    }

    @Override
    public String getDescription() {
        return "deletes birthday from given user";
    }

    @Override
    public List<OptionData> getOptions() {
        OptionData targetOption = new OptionData(OptionType.USER,"target", "Target user", true);
        return List.of(targetOption);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        Guild guild = event.getGuild();
        Member target = event.getOption("target").getAsMember();

        calendarBirthdayService.deleteBirthdayFromMember(guild.getIdLong(), target.getIdLong());
        event.getHook().editOriginal("Successfully deleted user's birthday").queue();
    }
}
