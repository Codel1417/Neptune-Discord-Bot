package neptune.commands.AdminCommands.LoggingOptions;

import java.io.IOException;
import neptune.commands.ICommand;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

public class enableLogging implements ICommand{
	protected static final Logger log = LogManager.getLogger();
    setLoggingChannel LoggingChannel = new setLoggingChannel();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
        guildentity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, true);
        GuildStorageHandler.getInstance().writeFile(guildentity);
        LoggingChannel.run(event,messageContent, builder);
        return builder.setContent("Server logging enabled.").build();
    }
}
