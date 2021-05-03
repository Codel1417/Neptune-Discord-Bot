package neptune.commands.AdminCommands.Options;

import java.io.IOException;
import neptune.commands.ICommand;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

public class disableLevelUpNotifications implements ICommand{
	protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try {
            guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
            guildentity.getGuildOptions().setOption(GuildOptionsEnum.LeaderboardLevelUpNotification, true);
            GuildStorageHandler.getInstance().writeFile(guildentity);
            event.getChannel().sendMessage("Leaderboard level-up notifications disabled.").queue();;
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }        
    }        
}
