package neptune.commands.AdminCommands.LoggingOptions;

import neptune.commands.ICommand;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class enableLogging implements ICommand{
	protected static final Logger log = LogManager.getLogger();
    final setLoggingChannel LoggingChannel = new setLoggingChannel();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        guildentity.getLogConfig().setGlobalLogging(true);
        guildDao.saveGuild(guildentity);
        LoggingChannel.run(event,messageContent, builder);
        return builder.setContent("Server logging enabled.").build();
    }
}
