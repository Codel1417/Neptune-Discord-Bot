import neptune.commands.CommandHandler;
import neptune.commands.CommandRegistry;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class testRegisteringCommands {
    @Test
    public void registerCommands(){
        CommandHandler commandRegistry = new CommandHandler();
        assertTrue(commandRegistry.isReady());
    }
}
