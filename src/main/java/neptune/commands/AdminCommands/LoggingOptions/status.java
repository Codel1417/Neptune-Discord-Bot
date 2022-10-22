package neptune.commands.AdminCommands.LoggingOptions;

import neptune.commands.Helpers;
import neptune.commands.ICommand;
import neptune.storage.dao.GuildDao;
import neptune.storage.entity.GuildEntity;
import neptune.storage.entity.LogOptionsEntity;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;

public class status implements ICommand {
    final Helpers helpers = new Helpers();
    protected static final Logger log = LogManager.getLogger();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        GuildDao guildDao = new GuildDao();
        GuildEntity guildentity = guildDao.getGuild(event.getGuild().getId());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");

        // logging status
        String LoggingChannel = guildentity.getLogConfig().getChannel();
        LogOptionsEntity logOptionsEntity = guildentity.getLogConfig();

        embedBuilder.addField("Global Logging Status",helpers.getEnabledDisabledIconText(logOptionsEntity.isGlobalLogging()),true);
        if (!LoggingChannel.equalsIgnoreCase("")) {
            embedBuilder.addField("Channel", event.getGuild().getTextChannelById(LoggingChannel).getAsMention(), true);
        }

        String logOptionsMessage = "Text Activity " + helpers.getEnabledDisabledIcon(logOptionsEntity.isTextChannelLogging()) + "\n" +
                "Voice Activity" + helpers.getEnabledDisabledIcon(logOptionsEntity.isVoiceChannelLogging()) + "\n" +
                "Member Activity" + helpers.getEnabledDisabledIcon(logOptionsEntity.isMemberActivityLogging()) + "\n" +
                "Server Changes " + helpers.getEnabledDisabledIcon(logOptionsEntity.isServerModificationLogging()) + "\n";
        embedBuilder.addField("Settings", logOptionsMessage,false);
        return builder.setEmbeds(embedBuilder.build()).build();
    }
}
