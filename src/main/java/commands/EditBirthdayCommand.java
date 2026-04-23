package commands;

import calendar.services.CalendarBirthdayService;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.DateTimeException;
import java.util.List;

public class EditBirthdayCommand implements IBotCommand{

    private static final Logger log = LoggerFactory.getLogger(EditBirthdayCommand.class);
    private final CalendarBirthdayService calendarBirthdayService;

    public EditBirthdayCommand(CalendarBirthdayService calendarBirthdayService){
        this.calendarBirthdayService = calendarBirthdayService;
    }

    @Override
    public DefaultMemberPermissions getPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public String getName() {
        return "editbirthday";
    }

    @Override
    public String getDescription() {
        return "edits birthday of given user";
    }

    @Override
    public List<SubcommandData> getSubcommands() {
        SubcommandData dateSubcommand = new SubcommandData("date", "Change birthday date")
                .addOption(OptionType.USER,"target", "Target user", true)
                .addOption(OptionType.INTEGER,"day", "Day of the month as an integer", true)
                .addOption(OptionType.INTEGER,"month", "Month as an integer (1-12)", true);
        SubcommandData messageSubcommand = new SubcommandData("message", "Change birthday message")
                .addOption(OptionType.USER,"target", "Target user", true)
                .addOption(OptionType.STRING,"message", "Message that will be sent on given date along with a ping", true);
        return List.of(dateSubcommand, messageSubcommand);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        String subcommand = event.getSubcommandName();
        if (subcommand == null) return;

        Guild guild = event.getGuild();
        Member target = event.getOption("target").getAsMember();

        switch(subcommand) {
            case "date":
                try {
                    int day = event.getOption("day").getAsInt();
                    int month = event.getOption("month").getAsInt();

                    //temporary hardcode to null till i add optional timezone option
                    if(calendarBirthdayService.rescheduleUsersBirthday(guild.getIdLong(), target.getIdLong(), month, day, null)){
                        event.getHook().editOriginal("Successfully edited date of user's birthday").queue();
                    }
                    else event.getHook().editOriginal("No birthday found for given user").queue();

                } catch (DateTimeException e) {
                    String message = e.getMessage();
                    event.getHook().editOriginal(message).queue();
                    log.error(message, e);
                }
                break;

            case "message":
                String message = event.getOption("message").getAsString();

                if(calendarBirthdayService.editUsersBirthday(guild.getIdLong(), target.getIdLong(), message)){
                    event.getHook().editOriginal("Successfully edited message in user's birthday").queue();
                }
                else event.getHook().editOriginal("No birthday found for given user").queue();
                break;
        }
    }
}
