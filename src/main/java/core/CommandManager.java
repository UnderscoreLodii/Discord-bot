package core;

import commands.IBotCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
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
        commands.get(event.getName()).execute(event);
    }

    public void registerCommands(CommandListUpdateAction updateAction){
        for(IBotCommand command : commands.values()){
            CommandData commandData = Commands.slash(command.getName(), command.getDescription())
                    .addOptions(command.getOptions())
                    .setDefaultPermissions(command.getPermissions());
            updateAction.addCommands(commandData);
        }
        updateAction.queue();
    }
}
