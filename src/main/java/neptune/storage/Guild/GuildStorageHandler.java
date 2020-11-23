package neptune.storage.Guild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import neptune.storage.Enum.GuildOptionsEnum;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class GuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    String guildsDir = "Guilds";

    public guildObject readFile(String guildID) throws IOException {

        /*
            Currently when an item from Jackson is cached. the entire file is not read. only the part that is accessed.
            While this is great for initial read performance this prevents caching the entire object

        */
        File file = new File(guildsDir + File.separator + guildID + ".yaml");
        guildObject guildEntity;

        log.debug("Reading File: " + file.getAbsolutePath());
        if (!file.exists()) {
            log.info("Adding guild: " + guildID);
            guildEntity = new guildObject(guildID);
            writeFile(guildEntity);
            return guildEntity;
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        guildEntity = om.readValue(file, guildObject.class);
        return guildEntity;
    }

    public void writeFile(guildObject guildEntity) throws IOException {

        File file = new File(guildsDir + File.separator + guildEntity.getGuildID() + ".yaml");
        file.getParentFile().mkdirs(); // makes required folders
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Writing File: " + file.getAbsolutePath());
        om.writeValue(file, guildEntity);
    }

    public void deserializationTest() {
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        guildObject guildObject = new guildObject("12345");
        guildObject.getGuildOptions().setOption(GuildOptionsEnum.LoggingEnabled, true);
        guildObject guildObjecta;
        try {
            guildStorageHandler.writeFile(guildObject);
            guildObjecta = guildStorageHandler.readFile("12345");
            System.out.println(
                    guildObjecta.getGuildOptions().getOption(GuildOptionsEnum.LoggingEnabled));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
