package neptune.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class GuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    String guildsDir = "Guilds";
    public guildObject readFile(String guildID) throws IOException {
        File file = new File(guildsDir + File.separator + guildID  +  ".yaml");

        // Instantiating a new ObjectMapper as a YAMLFactory
        if (!file.exists()){
            return null;
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Reading File: " +  file.getAbsolutePath());
        guildObject guildEntity = om.readValue(file,guildObject.class);
        return guildEntity;
    }
    public void writeFile(guildObject guildEntity) throws IOException {
        File file = new File(guildsDir + File.separator + guildEntity.getGuildID() + ".yaml");
        // Instantiating a new ObjectMapper as a YAMLFactory
        file.getParentFile().mkdirs(); //makes required folders
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Writing File: " +  file.getAbsolutePath());
        om.writeValue(file,guildEntity);

    }
    public static void main(String[] args){
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        guildObject guildObject = new guildObject("12345");
        try {
            guildStorageHandler.writeFile(guildObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
