package calendar.commands;

import calendar.services.CalendarBirthdayService;
import calendar.utils.UpcomingBirthdayView;
import commands.IBotCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;

public class ShowBirthdayCommand  implements IBotCommand {

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

        String subcommand = event.getSubcommandName();
        if (subcommand == null) return;

        Guild guild = event.getGuild();

        switch(subcommand) {
            case "foruser":
                Member target = event.getOption("target").getAsMember();

                UpcomingBirthdayView view = calendarBirthdayService.viewBirthdayForMember(guild.getIdLong(), target.getIdLong());

                if (view == null) event.getHook().editOriginal("No birthday found for given user").queue();
                else {

                    String message = "<@" + view.targetId() + ">" + "'s birthday is on " + view.day() + " " + view.month();
                    event.getHook()
                            .editOriginal(message)
                            .setAllowedMentions(Collections.emptySet())
                            .queue();
                }
                break;

            case "upcoming":
                var birthdays = calendarBirthdayService.viewUpcomingBirthdayList(guild.getIdLong());
                if (birthdays.isEmpty()) event.getHook().editOriginal("No birthdays registered in this server").queue();
                else {
                    EmbedBuilder eb = new EmbedBuilder();
                    StringBuilder sb = new StringBuilder();

                    for(int i = 0; i < 10 && i < birthdays.size(); i++) {
                        var bday = birthdays.get(i);
                        sb.append("<@").append(bday.targetId()).append("> ").append(bday.day()).append(" ").append(bday.month()).append("\n");
                    }

                    String message = sb.toString();
                    eb.setTitle("Upcoming birthdays")
                            .setDescription(message);

                    event.getHook().editOriginalEmbeds(eb.build()).queue();
                }
                break;
        }
    }
}