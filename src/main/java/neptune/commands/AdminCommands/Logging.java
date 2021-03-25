package neptune.commands.AdminCommands;

import neptune.commands.ICommand;
import neptune.commands.CommandHelpers;
import neptune.storage.Enum.LoggingOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.awt.*;
import java.io.IOException;

public class Logging extends CommandHelpers implements ICommand {
	protected static final Logger log = LogManager.getLogger();
    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
		try{
			String[] command = getCommandName(messageContent);
			boolean enabledOption = false;
			if (command[1].equalsIgnoreCase("enabled")) {
				enabledOption = true;
			}
			guildObject guildEntity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
			switch (command[0]) {
				case "global":
					if (enabledOption) {
						guildEntity.getLogOptions().setChannel(event.getChannel().getId());
						guildEntity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, enabledOption);
					} else {
						guildEntity.getLogOptions().setOption(LoggingOptionsEnum.GlobalLogging, enabledOption);
					}
					GuildStorageHandler.getInstance().writeFile(guildEntity);
					break;
				case "text":
					guildEntity.getLogOptions().setOption(LoggingOptionsEnum.TextChannelLogging, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildEntity);
					break;
				case "voice":
					guildEntity.getLogOptions().setOption(LoggingOptionsEnum.VoiceChannelLogging, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildEntity);
					break;
				case "member":
					guildEntity.getLogOptions().setOption(LoggingOptionsEnum.MemberActivityLogging, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildEntity);                    break;
				case "server":
					guildEntity.getLogOptions().setOption(LoggingOptionsEnum.ServerModificationLogging, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildEntity);
					break;
			}
			displayMenu(event, guildEntity);	
		}
		catch (IOException e){
			log.error(e);
		}
    }

    private void displayMenu(GuildMessageReceivedEvent event, guildObject guildEntity) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Logging Options");
        embedBuilder.setDescription("Controls Logging");

        // logging status
        String LoggingChannel = guildEntity.getLogOptions().getChannel();
        if (LoggingChannel == null) {
            LoggingChannel = "";
        }

        embedBuilder.addField(
                "Logging Status",
                getEnabledDisabledIconText(
                        guildEntity.getLogOptions().getOption(LoggingOptionsEnum.GlobalLogging)),
                true);
        if (!LoggingChannel.equalsIgnoreCase("")) {
            embedBuilder.addField(
                    "Channel",
                    event.getGuild().getTextChannelById(LoggingChannel).getAsMention(),
                    true);
        }

        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage
                .append("Text Activity ")
                .append(
                        getEnabledDisabledIcon(
                                guildEntity
                                        .getLogOptions()
                                        .getOption(LoggingOptionsEnum.TextChannelLogging)))
                .append("\n");
        logOptionsMessage
                .append("Voice Activity")
                .append(
                        getEnabledDisabledIcon(
                                guildEntity
                                        .getLogOptions()
                                        .getOption(LoggingOptionsEnum.VoiceChannelLogging)))
                .append("\n");
        logOptionsMessage
                .append("Member Activity")
                .append(
                        getEnabledDisabledIcon(
                                guildEntity
                                        .getLogOptions()
                                        .getOption(LoggingOptionsEnum.MemberActivityLogging)))
                .append("\n");
        logOptionsMessage
                .append("Server Changes ")
                .append(
                        getEnabledDisabledIcon(
                                guildEntity
                                        .getLogOptions()
                                        .getOption(LoggingOptionsEnum.ServerModificationLogging)))
                .append("\n");

        embedBuilder.addField("Logging Options", logOptionsMessage.toString(), false);

        String prefix = "!nep " + "log";
        embedBuilder.addField("Logging Commands", "", false);
        embedBuilder.addField("Enable Logging", prefix + " global <enabled/disabled>", true);
        embedBuilder.addField("Text Activity Logging", prefix + " text <enabled/disabled>", true);
        embedBuilder.addField("Voice Activity Logging", prefix + " voice <enabled/disabled>", true);
        embedBuilder.addField(
                "Member Activity Logging", prefix + " member <enabled/disabled>", true);
        embedBuilder.addField(
                "Server Changes Logging", prefix + " server <enabled/disabled>", true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
