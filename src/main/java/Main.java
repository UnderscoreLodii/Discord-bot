import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        String token = Dotenv.load().get("TOKEN");

        VoiceConnectionHandler voiceConnectionHandler = new VoiceConnectionHandler();
        CommandManager commandManager = new CommandManager(voiceConnectionHandler);
        MessageListener messageListener = new MessageListener();
        VoiceListener voiceListener = new VoiceListener(voiceConnectionHandler);

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT, GatewayIntent.GUILD_VOICE_STATES)
                .addEventListeners(commandManager, messageListener, voiceListener)
                .build();

        commandManager.registerCommands(jda.updateCommands());
    }
}
