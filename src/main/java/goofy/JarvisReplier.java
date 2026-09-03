//GOD CLASS FOR NOW DONT MIND IT
package goofy;

import com.google.genai.Client;
import com.google.genai.types.*;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.Member;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JarvisReplier {

    private static final Logger log = LoggerFactory.getLogger(JarvisReplier.class);
    private final Client client;
    private GenerateContentConfig generateConfig;

    public JarvisReplier(Client client) {
        this.client = client;
        this.generateConfig = generateConfig();
    }

    public String reply(Member member, String messageContents) {
        if(member.hasPermission(Permission.ADMINISTRATOR)){
            if(validateInput(messageContents)){
                try {
                    GenerateContentResponse response = client.models.generateContent(
                            "gemini-3.5-flash-lite",
                            messageContents,
                            generateConfig
                    );
                    return response.text();
                } catch (Exception e) {
                    log.error("Error, while generating response.", e);
                    return "Błąd podczas generowania odpowiedzi.";
                }
            } else {
                return "zły input bracie";
            }
        }
        else return "brat myśli że jest tony stark";
    }

    private boolean validateInput(String messageContents){
        return messageContents != null && messageContents.trim().length() > 2;
    }

    private GenerateContentConfig generateConfig(){
        Content systemInstruction = Content.fromParts(
                Part.fromText("Always answer in the exact same language used in the user's prompt. Answer directly and concisely in 1 to 2 sentences without pleasantries.")
        );

        return GenerateContentConfig.builder()
                .systemInstruction(systemInstruction)
                .maxOutputTokens(600)
                .temperature(0.5f)
                .build();
    }
}
