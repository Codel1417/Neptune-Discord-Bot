import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class guildStorageTests {

    @Test
    @Tag("DB")
    public void testAddGuild(){
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildID("1");
        GuildDao guildDao = new GuildDao();
        assertTrue(guildDao.saveGuild(guildEntity));
    }
    @Test
    @Tag("DB")
    public void testReadGuild(){
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildID("1");
        GuildDao guildDao = new GuildDao();
        assertTrue(guildDao.saveGuild(guildEntity));
        guildEntity = null; //reset variable
        guildEntity = guildDao.getGuild("1");
        assertNotNull(guildEntity);
    }
    @Test
    @Tag("DB")
    public void testLogEnabled(){
        GuildEntity guildEntity = new GuildEntity();
        guildEntity.setGuildID("1");
        guildEntity.getLogConfig().setChannel("1");
        guildEntity.getConfig().setLoggingEnabled(true);
        GuildDao guildDao = new GuildDao();
        assertTrue(guildDao.saveGuild(guildEntity));
    }
}
