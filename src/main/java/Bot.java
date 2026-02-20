import io.github.cdimascio.dotenv.DotenvEntry;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.Set;

public class Bot {
    private static String getToken(){
        return Dotenv.load().get("TOKEN");
    }
    static void main() {
        JDA api = JDABuilder.createDefault(getToken()).build();
    }
}
