package neptune.storage.Guild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.prometheus.client.cache.caffeine.CacheMetricsCollector;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class GuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    private String guildsDir = "Guilds";
    private static volatile GuildStorageHandler _instance;
    CacheMetricsCollector cacheMetrics = new CacheMetricsCollector().register();
    Cache<String, guildObject> cache = Caffeine.newBuilder()
        .expireAfterWrite(10, TimeUnit.MINUTES)
        .maximumSize(10_000)
        .recordStats()
        .build();
    private GuildStorageHandler() {
        new File(guildsDir).mkdirs(); // makes required folders
        cacheMetrics.addCache("GuildFilesCache", cache);
    }
   
    public static synchronized GuildStorageHandler getInstance() {
        if (_instance == null) {
            synchronized (GuildStorageHandler.class) {
                if (_instance == null) _instance = new GuildStorageHandler();
            }
        }
        return _instance;
    }
    public guildObject readFile(String guildID) throws IOException {

        File file = new File(guildsDir + File.separator + guildID + ".yaml");
        guildObject guildEntity = cache.getIfPresent(guildID);

        if (guildEntity != null) {
            return guildEntity;
        }
        log.debug("Reading File: " + file.getAbsolutePath());
        if (!file.exists()) {
            log.info("Adding guild: " + guildID);
            guildEntity = new guildObject(guildID);
            writeFile(guildEntity);
            return guildEntity;
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        guildEntity = om.readValue(file, guildObject.class);
        cache.put(guildID, guildEntity);
        return guildEntity;
    }

    public void writeFile(guildObject guildEntity) throws IOException {
        File file = new File(guildsDir + File.separator + guildEntity.getGuildID() + ".yaml");
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Writing File: " + file.getAbsolutePath());
        om.writeValue(file, guildEntity);
        cache.put(guildEntity.getGuildID(), guildEntity);
    }
}
