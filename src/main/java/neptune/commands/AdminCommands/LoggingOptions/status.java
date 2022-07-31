package neptune.commands.AdminCommands.LoggingOptions;

import neptune.commands.Helpers;
import neptune.commands.ICommand;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
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
        guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");

        // logging status
        String LoggingChannel = guildentity.getLogOptions().getChannel();

        embedBuilder.addField("Global Logging Status",helpers.getEnabledDisabledIconText(guildentity.getLogOptions().getOption(LoggingOptionsEnum.GlobalLogging)),true);
        if (!LoggingChannel.equalsIgnoreCase("")) {
            embedBuilder.addField("Channel", event.getGuild().getTextChannelById(LoggingChannel).getAsMention(), true);
        }

        String logOptionsMessage = "Text Activity " + helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.TextChannelLogging)) + "\n" +
                "Voice Activity" + helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.VoiceChannelLogging)) + "\n" +
                "Member Activity" + helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.MemberActivityLogging)) + "\n" +
                "Server Changes " + helpers.getEnabledDisabledIcon(guildentity.getLogOptions().getOption(LoggingOptionsEnum.ServerModificationLogging)) + "\n";
        embedBuilder.addField("Settings", logOptionsMessage,false);
        guildentity.closeSession();
        return builder.setEmbeds(embedBuilder.build()).build();
    }
}
