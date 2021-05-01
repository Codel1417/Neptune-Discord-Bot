package neptune.commands.AdminCommands.LoggingOptions;


import java.io.IOException;
import neptune.commands.ICommand;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class disableJoinLeaveLogging implements ICommand{
	protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try {
            guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
            guildentity.getLogOptions().setOption(LoggingOptionsEnum.MemberActivityLogging, true);
            GuildStorageHandler.getInstance().writeFile(guildentity);
            event.getChannel().sendMessage("Server logging disabled.").queue();;
        } catch (IOException e) {
            log.error(e);
        }    
    }
    
}
