import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import io.github.cdimascio.dotenv.Dotenv;

public class Bot extends ListenerAdapter {

    private static String getToken(){return Dotenv.load().get("TOKEN");}

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(getToken()).addEventListeners(new Bot()).build();

        CommandListUpdateAction commands = jda.updateCommands();

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
