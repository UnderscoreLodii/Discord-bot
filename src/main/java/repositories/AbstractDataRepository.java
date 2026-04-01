package repositories;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

abstract public class AbstractDataRepository<T> {
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
            } catch (IOException e) {
                System.err.println("Failed to save " + fileName + " " + e.getMessage());
                e.printStackTrace();
            }
        });
    }

    protected void load() {
        File file = new File("data/" + fileName);
        if(!file.exists()){
            System.err.println("Failed to load " + fileName + ", file doesn't exist");
            return;
        }
        try{
            obj = objectMapper.readValue(file, typeReference);
            System.out.println("Successfully loaded " + fileName);
        } catch (IOException e){
            System.err.println("Failed to load " + fileName + " " + e.getMessage());
            e.printStackTrace();
        }
    }
}
