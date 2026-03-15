package core;

import commands.DeleteIntroCommand;
import commands.IBotCommand;
import commands.SetIntroCommand;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import services.IntroService;

import java.util.HashMap;
import java.util.Map;

public class CommandManager extends ListenerAdapter {

    public final Map<String, IBotCommand> commands = new HashMap<>();

    public CommandManager(IntroService introService) {
        addCommand(new SetIntroCommand(introService));
        addCommand(new DeleteIntroCommand(introService));
    }

    private void addCommand(IBotCommand command){
        commands.put(command.getName(), command);
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        commands.get(event.getName()).execute(event);
    }


    public void registerCommands(CommandListUpdateAction updateAction){
        for(IBotCommand command : commands.values()){
            CommandData commandData = Commands.slash(command.getName(), command.getDescription()).addOptions(command.getOptions());
            updateAction.addCommands(commandData);
        }
        updateAction.queue();
    }
}
