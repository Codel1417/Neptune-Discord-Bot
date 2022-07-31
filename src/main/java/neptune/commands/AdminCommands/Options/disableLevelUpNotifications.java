package neptune.commands.AdminCommands.Options;

import neptune.commands.ICommand;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class disableLevelUpNotifications implements ICommand{
	protected static final Logger log = LogManager.getLogger();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
        guildentity.getGuildOptions().setOption(GuildOptionsEnum.LeaderboardLevelUpNotification, true);
        GuildStorageHandler.getInstance().writeFile(guildentity);
        guildentity.closeSession();
        return builder.setContent("Leaderboard level-up notifications disabled.").build();
    }
}
