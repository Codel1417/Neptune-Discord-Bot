package neptune.commands.AdminCommands.Options;

import java.io.IOException;

import neptune.commands.Helpers;
import neptune.commands.ICommand;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.awt.*;

public class status implements ICommand {
    final Helpers helpers = new Helpers();
    protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try {
            guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setColor(Color.MAGENTA);
            embedBuilder.setTitle("Options");
    
            StringBuilder logOptionsMessage = new StringBuilder();
            logOptionsMessage.append("Custom Roles ").append(helpers.getEnabledDisabledIcon(guildentity.getGuildOptions().getOption(GuildOptionsEnum.CustomRoleEnabled))).append("\n");
            logOptionsMessage.append("Leaderboard Level-Up Notifications").append(helpers.getEnabledDisabledIcon(guildentity.getGuildOptions().getOption(GuildOptionsEnum.LeaderboardLevelUpNotification))).append("\n");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
    
}
