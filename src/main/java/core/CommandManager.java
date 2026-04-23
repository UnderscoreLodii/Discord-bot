package core;

import commands.IBotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {

    public final Map<String, IBotCommand> commands = new HashMap<>();

    public CommandManager addCommand(IBotCommand command){
        commands.put(command.getName(), command);
        return this;
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        IBotCommand command = commands.get(event.getName());
        if (command == null) {
            event.reply("❌ This command no longer exists or is being updated!").queue();
            return;
        }
        command.execute(event);
    }

    public void registerCommands(CommandListUpdateAction updateAction){
        for(IBotCommand command : commands.values()){
            var commandData = Commands.slash(command.getName(), command.getDescription())
                    .setDefaultPermissions(command.getPermissions());

            if (!command.getSubcommands().isEmpty()) commandData.addSubcommands(command.getSubcommands());
            else if (!command.getOptions().isEmpty()) commandData.addOptions(command.getOptions());

            updateAction.addCommands(commandData);
        }
        updateAction.queue();
    }
}
