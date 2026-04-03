package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import services.CalendarService;
import calendar.utils.DateTimeParser;

import java.time.DateTimeException;
import java.time.ZonedDateTime;
import java.util.List;

public class SetBirthdayCommand implements IBotCommand {

    private final CalendarService calendarService;

    public SetBirthdayCommand(CalendarService calendarService){
        this.calendarService = calendarService;
    }

    @Override
    public DefaultMemberPermissions getPermissions() {
        return DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR);
    }

    @Override
    public String getName() {
        return "setbirthday";
    }

    @Override
    public String getDescription() {
        return "sets birthday for given user";
    }

    @Override
    public List<OptionData> getOptions() {
        OptionData dayOption = new OptionData(OptionType.INTEGER,"day", "Day of the month as an integer", true);
        OptionData monthOption = new OptionData(OptionType.INTEGER,"month", "Month as an integer (1-12)", true);
        OptionData targetOption = new OptionData(OptionType.USER,"target", "Target user", true);
        OptionData messageOption = new OptionData(OptionType.STRING,"message", "Message that will be sent on given date along with a ping");
        return List.of(dayOption, monthOption, targetOption, messageOption);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        try {
            Guild guild = event.getGuild();
            Member target = event.getOption("target").getAsMember();
            Integer day = event.getOption("day").getAsInt();
            Integer month = event.getOption("month").getAsInt();
            OptionMapping messageOption = event.getOption("message");
            String message = "Happy Birthday!";

            if(messageOption != null){
                message = event.getOption("message").getAsString();
            }

            //temorarily hardcoded timezone
            ZonedDateTime time = DateTimeParser.parseDateTime("Europe/Warsaw", "00:00", day, month);
            boolean isLeap = (day == 29 && month == 2);
            calendarService.setBirthday(guild.getIdLong(), time, target.getIdLong(), message, isLeap);
            event.getHook().editOriginal("Successfully set user's birthday").queue();
        }
        catch(DateTimeException e){
            String message = e.getMessage();
            event.getHook().editOriginal(message).queue();
            System.err.println(message);
        }
    }
}
