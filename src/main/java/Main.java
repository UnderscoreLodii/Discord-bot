import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        String token = Dotenv.load().get("TOKEN");

        CommandManager commandManager = new CommandManager();
        MessageListener messageListener = new MessageListener();

        JDA jda = JDABuilder.createDefault(token)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(commandManager, messageListener)
                .build();

        commandManager.registerCommands(jda.updateCommands());
    }
}
