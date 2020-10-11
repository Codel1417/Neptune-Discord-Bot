package neptune.storage;

import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.module.paranamer.ParanamerModule;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalCause;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import neptune.storage.Enum.GuildOptionsEnum;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    String guildsDir = "Guilds";

    Cache<String, guildObject> cache = Caffeine.newBuilder()
    .expireAfterAccess(30, TimeUnit.MINUTES)
    .removalListener((String guildID, guildObject guildEntity, RemovalCause cause) ->
    log.debug("Key " + guildID + " was removed from cache: " + cause))
    .maximumSize(100)
    .build();

    public guildObject readFile(String guildID) throws IOException {
        File file = new File(guildsDir + File.separator + guildID + ".yaml");
        guildObject guildEntity;

        //try cache
        guildEntity =  cache.getIfPresent(guildID);
        if (guildEntity != null){
            log.debug("Reading From Cache: " + guildEntity.getGuildID());
            return guildEntity;
        }

        log.debug("Reading File: " + file.getAbsolutePath());
        // Instantiating a new ObjectMapper as a YAMLFactory
        if (!file.exists()) {
            log.info("Adding guild: " + guildID);
            guildEntity = new guildObject(guildID);
            writeFile(guildEntity);
            return guildEntity;
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.registerModule(new ParanamerModule());
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        guildEntity = om.readValue(file, guildObject.class);
        return guildEntity;
    }

    public void writeFile(guildObject guildEntity) throws IOException {
        cache.invalidate(guildEntity.getGuildID())
        cache.put(guildEntity.getGuildID(), guildEntity); //write to cache 

        File file = new File(guildsDir + File.separator + guildEntity.getGuildID() + ".yaml");
        // Instantiating a new ObjectMapper as a YAMLFactory
        file.getParentFile().mkdirs(); // makes required folders
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
        log.debug("Writing File: " + file.getAbsolutePath());
        om.writeValue(file, guildEntity);

    }

    public void deserializationTest(){
        GuildStorageHandler guildStorageHandler = new GuildStorageHandler();
        guildObject guildObject = new guildObject("12345");
        guildObject.getGuildOptions().setOption(GuildOptionsEnum.LoggingEnabled, true);
        guildObject guildObjecta;
        try {
            guildStorageHandler.writeFile(guildObject);
            guildObjecta = guildStorageHandler.readFile("12345");
            System.out.println(guildObjecta.getGuildOptions().getOption(GuildOptionsEnum.LoggingEnabled));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
