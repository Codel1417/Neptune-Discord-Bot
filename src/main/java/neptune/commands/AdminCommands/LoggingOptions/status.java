package neptune.commands.AdminCommands.LoggingOptions;

import java.io.IOException;

import neptune.commands.Helpers;
import neptune.commands.ICommand;
import neptune.storage.Enum.LoggingOptionsEnum;
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
            embedBuilder.setTitle("Logging Options");
    
            // logging status
            String LoggingChannel = guildentity.getLogOptions().getChannel();
            if (LoggingChannel == null) {
                LoggingChannel = "";
            }
    
            embedBuilder.addField("Global Logging Status",helpers.getEnabledDisabledIconText(guildentity.getLogOptions().getOption(LoggingOptionsEnum.GlobalLogging)),true);
            if (!LoggingChannel.equalsIgnoreCase("")) {
                embedBuilder.addField(
                        "Channel",
                        event.getGuild().getTextChannelById(LoggingChannel).getAsMention(),
                        true);
            }
    
            StringBuilder logOptionsMessage = new StringBuilder();
            logOptionsMessage.append("Text Activity ").append(helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.TextChannelLogging))).append("\n");
            logOptionsMessage.append("Voice Activity").append(helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.VoiceChannelLogging))).append("\n");
            logOptionsMessage.append("Member Activity").append(helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.MemberActivityLogging))).append("\n");
            logOptionsMessage.append("Server Changes ").append(helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.ServerModificationLogging))).append("\n");
            embedBuilder.addField("Settings", logOptionsMessage.toString(),false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
    
}
