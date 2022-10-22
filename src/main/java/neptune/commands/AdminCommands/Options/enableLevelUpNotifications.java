package neptune.commands.AdminCommands.Options;

import neptune.commands.ICommand;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class enableLevelUpNotifications implements ICommand {
	protected static final Logger log = LogManager.getLogger();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        guildentity.getConfig().setLeaderboardLevelUpNotification(true);
        guildDao.saveGuild(guildentity);
        return builder.setContent("Leaderboard level-up notifications enabled.").build();
    }
}
