package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract public class AbstractDataRepository<T> {
    private final Logger log = LoggerFactory.getLogger(getClass());
    protected final ObjectMapper objectMapper = new ObjectMapper();
    private final ExecutorService saveExecutor = Executors.newSingleThreadExecutor();
    protected T obj;
    protected String fileName;
    private final TypeReference<T> typeReference;

    public AbstractDataRepository(TypeReference<T> typeReference, String fileName) {
        this.fileName = fileName;
        this.typeReference = typeReference;
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
    }

    protected void save() {
        saveExecutor.execute(() -> {
            try {
                File file = new File("data/" + fileName);
                if (file.getParentFile() != null) {
                    file.getParentFile().mkdirs();
                }
                objectMapper.writeValue(file, obj);
                log.debug("Successfully saved {}", fileName);
            } catch (IOException e) {
                log.error("Failed to save {}", fileName, e);
            }
        });
    }

    protected void load() {
        File file = new File("data/" + fileName);
        if(!file.exists()){
            log.warn("Creating fresh database, {} doesn't exist", fileName);
            return;
        }
        try{
            obj = objectMapper.readValue(file, typeReference);
            log.info("Successfully loaded {}", fileName);
        } catch (IOException e){
            log.error("Failed to load {}", fileName, e);
        }
    }
}
