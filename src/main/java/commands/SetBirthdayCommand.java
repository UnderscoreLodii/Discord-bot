package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import calendar.services.CalendarBirthdayService;

import java.time.DateTimeException;
import java.util.List;

public class SetBirthdayCommand implements IBotCommand {

    private static final Logger log = LoggerFactory.getLogger(SetBirthdayCommand.class);
    private final CalendarBirthdayService calendarBirthdayService;

    public SetBirthdayCommand(CalendarBirthdayService calendarBirthdayService){
        this.calendarBirthdayService = calendarBirthdayService;
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
        OptionData targetOption = new OptionData(OptionType.USER,"target", "Target user", true);
        OptionData dayOption = new OptionData(OptionType.INTEGER,"day", "Day of the month as an integer", true);
        OptionData monthOption = new OptionData(OptionType.INTEGER,"month", "Month as an integer (1-12)", true);
        OptionData messageOption = new OptionData(OptionType.STRING,"message", "Message that will be sent on given date along with a ping");
        return List.of(targetOption, dayOption, monthOption, messageOption);
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        if(event.getMember()==null) return;
        event.deferReply(true).queue();

        try {
            Guild guild = event.getGuild();
            Member target = event.getOption("target").getAsMember();
            int day = event.getOption("day").getAsInt();
            int month = event.getOption("month").getAsInt();
            OptionMapping messageOption = event.getOption("message");
            String message = "Happy Birthday!";

            if(messageOption != null){
                message = event.getOption("message").getAsString();
            }

            //temporary hardcode to null till i add optional timezone option
            if (calendarBirthdayService.setBirthday(guild.getIdLong(), target.getIdLong(), month, day, null, message)){
                event.getHook().editOriginal("Successfully set user's birthday").queue();
            }
            else event.getHook().editOriginal("User already has a birthday set, please use /deleteBirthday or /editBirthday").queue();
        }
        catch(DateTimeException e){
            String message = e.getMessage();
            event.getHook().editOriginal(message).queue();
            log.error(message, e);
        }
    }
}
