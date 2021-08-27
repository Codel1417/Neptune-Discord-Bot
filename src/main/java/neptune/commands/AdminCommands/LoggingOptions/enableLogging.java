package neptune.commands.AdminCommands.LoggingOptions;

import java.io.IOException;
import neptune.commands.ICommand;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

public class enableLogging implements ICommand{
	protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try {
            guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
            guildentity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, true);
            GuildStorageHandler.getInstance().writeFile(guildentity);
            guildentity.closeSession();
            event.getChannel().sendMessage("Server logging enabled.").queue();
        } catch (Exception e) {
            log.error(e);
            Sentry.captureException(e);
        }        
    }
}
