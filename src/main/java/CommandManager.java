import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;

public class CommandManager extends ListenerAdapter {

    public void registerCommands(CommandListUpdateAction commands){
        commands.addCommands(Commands.slash("greet", "Sends a hi message, pinging user that sent that message"));

        commands.queue();
    }

    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event){
        switch(event.getName()){
            case "greet":
                Member member = event.getMember();
                greetMember(event, member);
                break;
        }
    }

    public void greetMember(SlashCommandInteractionEvent event, Member member){
        event.reply("Hi " + member.getAsMention()).queue();
    }
}
