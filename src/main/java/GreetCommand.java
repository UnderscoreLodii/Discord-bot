import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.Collections;
import java.util.List;

public class GreetCommand implements IBotCommand {

    @Override
    public String getName() {
        return "greet";
    }

    @Override
    public String getDescription() {
        return "Sends a hi message, pinging the user that sent it";
    }

    @Override
    public List<OptionData> getOptions() {
        return Collections.emptyList();
    }

    @Override
    public void execute(SlashCommandInteractionEvent event) {
        event.reply("Hi " + event.getMember().getAsMention()).queue();
    }
}
