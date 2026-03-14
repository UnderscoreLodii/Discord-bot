import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class IntroDataManager {
    private Map<Long, Map<Long, String>> intros = new HashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();

    public IntroDataManager() {
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        load();
    }

    public Set<Long> getGuilds(){
        return intros.keySet();
    }

    public Map<Long, String> getIntrosForGivenGuildId(Long guildId) {
        return intros.get(guildId);

    }

    public void addIntro(Long guildId, Long userId, String intro){
        intros.computeIfAbsent(guildId, e -> new HashMap<>()).put(userId, intro);
        saveExecutor.execute(this::save);
    }

    private void save() {
        try {
            objectMapper.writeValue(new File("data/intros.json"), intros);
        } catch (IOException e) {
            System.err.println("Failed to save intros to JSON" + e.getMessage());
            e.printStackTrace();
        }
    }

    private void load(){
        File file = new File("data/intros.json");
        if(!file.exists()){
            System.err.println("Failed to load intros from JSON, file doesn't exist");
            return;
        }
        try{
            TypeReference<Map<Long, Map<Long, String>>> typeRef = new TypeReference<>() {};
            intros = objectMapper.readValue(file, typeRef);
        } catch (IOException e){
            System.err.println("Failed to load intros from JSON" + e.getMessage());
            e.printStackTrace();
        }
    }
}
