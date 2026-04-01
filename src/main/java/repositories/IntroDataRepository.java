package repositories;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.concurrent.ConcurrentHashMap;

public class IntroDataRepository extends AbstractDataRepository<ConcurrentHashMap<Long, ConcurrentHashMap<Long, String>>>{

    public IntroDataRepository() {
        super(new TypeReference<ConcurrentHashMap<Long, ConcurrentHashMap<Long, String>>>() {}, "intros");
        load();
        if (this.obj == null) this.obj = new ConcurrentHashMap<>();
    }

    public String getIntro(Long guildId, Long userId) {
        var guildIntros = obj.get(guildId);
        if (guildIntros != null) return guildIntros.get(userId);
        else return null;
    }

    public void addIntro(Long guildId, Long userId, String intro){
        obj.computeIfAbsent(guildId, e -> new ConcurrentHashMap<>()).put(userId, intro);
        save();
    }

    public void deleteIntro(Long guildId, Long userId){
        var guildIntros = obj.get(guildId);
        if (guildIntros != null) {
            guildIntros.remove(userId);
            if (guildIntros.isEmpty()) obj.remove(guildId);
            save();
        }
    }
}
