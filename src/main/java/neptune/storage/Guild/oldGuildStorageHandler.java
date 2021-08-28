package neptune.storage.Guild;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.io.File;
import java.io.IOException;

public class oldGuildStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    private final String guildsDir = "Guilds";
    private static volatile oldGuildStorageHandler _instance;
    private oldGuildStorageHandler() {
        new File(guildsDir).mkdirs(); // makes required folders
    }
   
    public static synchronized oldGuildStorageHandler getInstance() {
        if (_instance == null) {
            synchronized (oldGuildStorageHandler.class) {
                if (_instance == null) _instance = new oldGuildStorageHandler();
            }
        }
        return _instance;
    }
    public guildObject readFile(String guildID)  {


        try {
            File file = new File(guildsDir + File.separator + guildID + ".yaml");
            guildObject guildEntity;
            log.debug("Reading File: " + file.getAbsolutePath());
            Sentry.addBreadcrumb("Reading File: " + file.getAbsolutePath());

            if (!file.exists()) {
                return null;
            }
            ObjectMapper om = new ObjectMapper(new YAMLFactory());
            guildEntity = om.readValue(file, guildObject.class);
            return guildEntity;
        }
        catch (IOException e){
            log.error(e);
            Sentry.captureException(e);
        }
        return null;
    }

}
