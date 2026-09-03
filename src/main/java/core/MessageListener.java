package core;

import goofy.JarvisReplier;
import messages.PingReceiver;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.concurrent.CompletableFuture;

public class MessageListener extends ListenerAdapter {

    private final PingReceiver pingReceiver;
    private final JarvisReplier jarvisReplier;

    public MessageListener(PingReceiver pingReceiver, JarvisReplier jarvisReplier) {
        this.pingReceiver = pingReceiver;
        this.jarvisReplier = jarvisReplier;
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event){
        if(event.getAuthor().isBot()) return;
        if(!event.isFromGuild()) return;

        Message message = event.getMessage();
        Member member = event.getMember();
        if (member == null) return;

        if(message.getMentions().isMentioned(event.getJDA().getSelfUser(), Message.MentionType.USER)){
            pingReceiver.handlePing(event, message);
        }
        else {
            String messageContents = message.getContentDisplay();
            if (messageContents.startsWith("Jarvis, ")) {
                message.reply("*Jarvis thinking*").queue(sentMessage ->{
                    CompletableFuture.runAsync(() -> {
                        String reply = jarvisReplier.reply(member, messageContents.substring(8));
                        sentMessage.editMessage(reply)
                                .queue();
                    });
                });
            } else if (member.getRoles().stream().anyMatch(role -> role.getName().equals("cwel")) && message.getChannel().getName().equals("srogólne")) {
                if (Math.random() * 2001 > 1999) {
                    message.reply("https://cdn.discordapp.com/attachments/1269648866244825211/1348033993177829386/caption.gif?ex=699eb6bd&is=699d653d&hm=c719f641d1d787bfa082e93630b734b27dc5b03e5e2e4fdb307799a9093427b2&")
                            .queue();
                }
            }
        }
    }
}
