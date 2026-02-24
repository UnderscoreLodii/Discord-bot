import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.regex.*;

public class Bot extends ListenerAdapter {

    private static String getToken(){return Dotenv.load().get("TOKEN");}

    public static void main(String[] args) {
        JDA jda = JDABuilder.createDefault(getToken())
                .addEventListeners(new Bot())
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .build();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(Commands.slash("greet", "Sends a hi message, pinging user that sent that message"));

        commands.queue();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;

        Message message = event.getMessage();

        if(message.getMentions().isMentioned(event.getJDA().getSelfUser())){
            replyToMessage(event, message);
        }
        else if(message.getMember().getRoles().stream().anyMatch(role->role.getName().equals("cwel")) && message.getChannel().getName().equals("srogólne")){
            if (Math.random()*2001>1999){
                message.reply("https://cdn.discordapp.com/attachments/1269648866244825211/1348033993177829386/caption.gif?ex=699eb6bd&is=699d653d&hm=c719f641d1d787bfa082e93630b734b27dc5b03e5e2e4fdb307799a9093427b2&")
                        .queue();
            }
        }
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

    public void replyToMessage(MessageReceivedEvent event, Message message){
        String messageContents = message.getContentDisplay();
        Member author = event.getMember();
        if(Pattern.compile("spierdalaj", Pattern.CASE_INSENSITIVE).matcher(messageContents).find()){
            if(author.hasPermission(Permission.ADMINISTRATOR)){
                message.reply("No dobra").queue();
            }
            else{
                author.timeoutFor(Duration.ofSeconds(30)).reason("Łatwo").queue();
                message.reply("Łap muta cwelu").queue();
            }
        }
        else message.reply("Nie rozumim :skull:").queue();
    }
}
