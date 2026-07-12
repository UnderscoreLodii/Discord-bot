package commands;

import calendar.services.CalendarBirthdayService;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ShowBirthdayCommand  implements IBotCommand{

    private static final Logger log = LoggerFactory.getLogger(ShowBirthdayCommand.class);
    private final CalendarBirthdayService calendarBirthdayService;

    public ShowBirthdayCommand(CalendarBirthdayService calendarBirthdayService){
        this.calendarBirthdayService = calendarBirthdayService;
    }

    @Override
    public DefaultMemberPermissions getPermissions() {
        return DefaultMemberPermissions.ENABLED;
    }

    @Override
    public String getName() {
        return "showbirthday";
    }

    @Override
    public String getDescription() {
        return "command for viewing birthdays";
    }

    @Override
    public List<SubcommandData> getSubcommands() {
        SubcommandData forUserSubcommand = new SubcommandData("foruser", "Show birthday for given user")
                .addOption(OptionType.USER,"target", "Target user", true);
        SubcommandData upcomingSubcommand = new SubcommandData("upcoming", "Show list of upcoming birthdays");
        return List.of(forUserSubcommand,upcomingSubcommand);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();
    }
}