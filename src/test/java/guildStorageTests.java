import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class guildStorageTests {

    @Test
    @Tag("DB")
    public void testAddGuild(){
        guildObject guildEntity = new guildObject("1");
        GuildStorageHandler guildStorageHandler = GuildStorageHandler.getInstance();
        assertTrue(guildStorageHandler.writeFile(guildEntity));
    }
    @Test
    @Tag("DB")
    public void testReadGuild(){
        guildObject guildEntity = new guildObject("1");
        GuildStorageHandler guildStorageHandler = GuildStorageHandler.getInstance();
        assertTrue(guildStorageHandler.writeFile(guildEntity));
        guildEntity = null; //reset variable
        guildEntity = guildStorageHandler.readFile("1");
        assertNotNull(guildEntity);
    }
    @Test
    @Tag("DB")
    public void testLogEnabled(){
        guildObject guildEntity = new guildObject("1");
        guildEntity.getLogOptions().setChannel("1");
        guildEntity.getGuildOptions().setOption(GuildOptionsEnum.LoggingEnabled,true);
        GuildStorageHandler guildStorageHandler = GuildStorageHandler.getInstance();
        assertTrue(guildStorageHandler.writeFile(guildEntity));
    }
}
