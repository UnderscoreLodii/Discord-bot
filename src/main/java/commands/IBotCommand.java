package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import java.util.List;

public interface IBotCommand {
    DefaultMemberPermissions getPermissions();
    String getName();
    String getDescription();
    List<OptionData> getOptions();
    void execute(SlashCommandInteractionEvent event);
}
