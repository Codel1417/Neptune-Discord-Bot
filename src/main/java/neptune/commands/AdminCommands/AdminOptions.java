package neptune.commands.AdminCommands;

import neptune.commands.ICommand;
import neptune.commands.CommandHelpers;
import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Guild.GuildStorageHandler;
import neptune.storage.Guild.guildObject;
import neptune.storage.Guild.guildObject.guildOptionsObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.IOException;

public class AdminOptions extends CommandHelpers implements ICommand {
	protected static final Logger log = LogManager.getLogger();

    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
		try{
			String[] CommandArray = getCommandName(messageContent);


			boolean enabledOption = false;
			if (CommandArray[1].equalsIgnoreCase("enabled")) {
			enabledOption = true;
			}
			guildObject guildentity = GuildStorageHandler.getInstance().readFile(event.getGuild().getId());
			switch (CommandArray[0].toLowerCase()) {
				case "customrole":
					guildentity.getGuildOptions().setOption(GuildOptionsEnum.CustomRoleEnabled, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildentity);
					break;
				case "levelUp":
					guildentity.getGuildOptions().setOption(GuildOptionsEnum.LeaderboardLevelUpNotification, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildentity);
					break;
				case "leaderboards":
					guildentity.getGuildOptions().setOption(GuildOptionsEnum.leaderboardEnabled, enabledOption);
					GuildStorageHandler.getInstance().writeFile(guildentity);
					break;
			}
			displayMenu(event, guildentity.getGuildOptions());
		}
		catch (IOException e){
			log.error(e);
		}
    }

    private void displayMenu(
            GuildMessageReceivedEvent event, guildOptionsObject guildOptionsEntity) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Bot Options");
        embedBuilder.setDescription("Controls Neptune's additional features.");

        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage
                .append("Custom Role ")
                .append(
                        getEnabledDisabledIcon(
                                guildOptionsEntity.getOption(GuildOptionsEnum.CustomRoleEnabled)))
                .append("\n");
        logOptionsMessage
                .append("Leaderboards ")
                .append(
                        getEnabledDisabledIcon(
                                guildOptionsEntity.getOption(GuildOptionsEnum.leaderboardEnabled)))
                .append("\n");
        logOptionsMessage
                .append("Level Up Notifications")
                .append(
                        getEnabledDisabledIcon(
                                guildOptionsEntity.getOption(
                                        GuildOptionsEnum.LeaderboardLevelUpNotification)))
                .append("\n");

        embedBuilder.addField("Logging Options", logOptionsMessage.toString(), false);

        String prefix = "!nep " + "options";
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("Enable Custom Roles:")
                .append(prefix)
                .append(" customrole <enabled/disabled>\n");
        stringBuilder
                .append("Enable Level Up Notifications: ")
                .append(prefix)
                .append(" levelup <enabled/disabled>\n");
        stringBuilder
                .append("Enable Leaderboards: ")
                .append(prefix)
                .append(" leaderboards <enabled/disabled>\n");
        embedBuilder.addField("Admin Commands", stringBuilder.toString(), false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
