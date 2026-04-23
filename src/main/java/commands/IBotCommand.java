package commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SubcommandData;

import java.util.Collections;
import java.util.List;

public interface IBotCommand {
    DefaultMemberPermissions getPermissions();
    String getName();
    String getDescription();
    default List<OptionData> getOptions(){
        return Collections.emptyList();
    }
    default List<SubcommandData> getSubcommands(){
        return Collections.emptyList();
    }
    void execute(SlashCommandInteractionEvent event);
}
