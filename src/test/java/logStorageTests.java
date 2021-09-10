import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.logObject;
import neptune.storage.logsStorageHandler;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class logStorageTests
{
    @Test
    @Tag("DB")
    public void testWriteLog() throws IOException {
        GuildCreator();
        logObject logEntity = new logObject();
        logEntity.setChannelID("1");
        logEntity.setGuildID("1");
        logEntity.setMemberID("1");
        logEntity.setMessageID("1");
        logEntity.setMessageContent("Test");
        logsStorageHandler logsStorageHandler = neptune.storage.logsStorageHandler.getInstance();
        logsStorageHandler.writeFile(logEntity);
    }
    @Test
    @Tag("DB")
    public void testReadLog() throws IOException {
        GuildCreator();
        logObject logEntity = new logObject();
        logEntity.setChannelID("1");
        logEntity.setGuildID("1");
        logEntity.setMemberID("1");
        logEntity.setMessageID("1");
        logEntity.setMessageContent("Test");
        logsStorageHandler logsStorageHandler = neptune.storage.logsStorageHandler.getInstance();
        logsStorageHandler.deleteFile("1"); //Delete entry if already exists
        logsStorageHandler.writeFile(logEntity);

        logsStorageHandler logsStorageHandler2 = neptune.storage.logsStorageHandler.getInstance();
        logObject logEntity2 = logsStorageHandler2.readFile("1");
        assertNotNull(logEntity2);
        //compare all values
        assertEquals(logEntity.getChannelID(),logEntity2.getChannelID());
        assertEquals(logEntity.getGuildID(),logEntity2.getGuildID());
        assertEquals(logEntity.getMemberID(),logEntity2.getMemberID());
        assertEquals(logEntity.getMessageID(),logEntity2.getMessageID());
        assertEquals(logEntity.getMessageContent(),logEntity2.getMessageContent());
        assertEquals(logEntity.getTimestamp(),logEntity2.getTimestamp());
    }
    private void GuildCreator(){
        GuildStorageHandler guildStorageHandler = GuildStorageHandler.getInstance();
        guildObject guildEntity = guildStorageHandler.readFile("1");
        guildStorageHandler.writeFile(guildEntity);
    }

}
