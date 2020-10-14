package neptune.storage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

// root/logs/guildID/MessageID.yaml
public class logsStorageHandler {
    protected static final Logger log = LogManager.getLogger();
    String logsDir = "Logs";

    public void writeFile(logObject logEntity) throws IOException {
        File file =
                new File(
                        logsDir
                                + File.separator
                                + logEntity.getGuildID()
                                + File.separator
                                + logEntity.getChannelID()
                                + File.separator
                                + logEntity.getMessageID()
                                + ".yaml");
        // Instantiating a new ObjectMapper as a YAMLFactory
        file.getParentFile().mkdirs(); // makes required folders
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Writing File: " + file.getAbsolutePath());
        om.writeValue(file, logEntity);
    }

    public logObject readFile(String messageID, String guildID, String channelID)
            throws IOException {
        File file =
                new File(
                        logsDir
                                + File.separator
                                + guildID
                                + File.separator
                                + channelID
                                + File.separator
                                + messageID
                                + ".yaml");
        // Instantiating a new ObjectMapper as a YAMLFactory
        if (!file.exists()) {
            return null;
        }
        ObjectMapper om = new ObjectMapper(new YAMLFactory());
        log.debug("Reading File: " + file.getAbsolutePath());
        logObject logEntity = om.readValue(file, logObject.class);
        return logEntity;
    }

    public void deleteFile(String messageID, String guildID, String channelID) {
        File file =
                new File(
                        logsDir
                                + File.separator
                                + guildID
                                + File.separator
                                + channelID
                                + File.separator
                                + messageID
                                + ".yaml");
        if (file.exists()) {
            log.debug("Deleting File: " + file.getAbsolutePath());
            file.delete();
        }
    }

    public void deleteChannel(String guildID, String channelID) {
        File file = new File(logsDir + File.separator + guildID + File.separator + channelID);
        try {
            log.debug("Deleting Logs for Channel: " + file.getAbsolutePath());
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }

    public void deleteGuild(String guildID) {
        File file = new File(logsDir + File.separator + guildID);
        try {
            log.debug("Deleting Logs for Guild: " + file.getAbsolutePath());
            FileUtils.deleteDirectory(file);
        } catch (IOException e) {
            log.error(e.toString());
        }
    }
}
