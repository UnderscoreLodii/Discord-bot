import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        String token = Dotenv.load().get("TOKEN");


        CommandManager commandManager = new CommandManager();
        MessageListener messageListener = new MessageListener();

        VoiceConnectionHandler voiceConnectionHandler = new VoiceConnectionHandler();
        VoiceJoinIntroManager voiceJoinIntroManager = new VoiceJoinIntroManager(voiceConnectionHandler);
        VoiceListener voiceListener = new VoiceListener(voiceJoinIntroManager);

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(commandManager, messageListener, voiceListener)
                .build();

        commandManager.registerCommands(jda.updateCommands());
    }
}
