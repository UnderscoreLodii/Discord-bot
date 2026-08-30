package messages;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.time.Duration;
import java.util.regex.Pattern;

public class PingReceiver {

    //temporary version
    public void handlePing(MessageReceivedEvent event, Message message){
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
