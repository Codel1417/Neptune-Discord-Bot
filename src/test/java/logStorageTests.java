import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import neptune.storage.entity.LogEntity;
import neptune.storage.dao.LogsDao;
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
        LogEntity logEntity = new LogEntity();
        logEntity.setChannelID("1");
        logEntity.setGuildID("1");
        logEntity.setMemberID("1");
        logEntity.setMessageID("1");
        logEntity.setMessageContent("Test");
        LogsDao LogsDao = new LogsDao();
        LogsDao.writeFile(logEntity);
    }
    @Test
    @Tag("DB")
    public void testReadLog() throws IOException {
        GuildCreator();
        LogEntity logEntity = new LogEntity();
        logEntity.setChannelID("1");
        logEntity.setGuildID("1");
        logEntity.setMemberID("1");
        logEntity.setMessageID("1");
        logEntity.setMessageContent("Test");
        LogsDao LogsDao = new LogsDao();
        LogsDao.deleteFile("1"); //Delete entry if already exists
        LogsDao.writeFile(logEntity);

        LogsDao logsDao2 = new LogsDao();
        LogEntity logEntity2 = logsDao2.readFile("1");
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
        GuildDao guildDao = new GuildDao();
        GuildEntity guildEntity = guildDao.getGuild("1");
        guildDao.saveGuild(guildEntity);
    }

}
