package neptune.commands.AdminCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import neptune.storage.guildOptionsObject;
import neptune.storage.Enum.GuildOptionsEnum;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class AdminOptions extends CommonMethods implements CommandInterface {
  @Override
  public String getName() {
    return "Admin Menu";
  }

  @Override
  public String getCommand() {
    return "options";
  }

  @Override
  public String getDescription() {
    return "Shows server admin options menu";
  }

  @Override
  public commandCategories getCategory() {
    return commandCategories.Admin;
  }

  @Override
  public String getHelp() {
    return "";
  }

  @Override
  public boolean getRequireManageServer() {
    return true;
  }

  @Override
  public boolean getHideCommand() {
    return false;
  }

  @Override
  public boolean getRequireManageUsers() {
    return false;
  }

  @Override
  public guildObject run(
      GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
    String[] CommandArray = getCommandName(messageContent);
    guildOptionsObject guildoptionsEntity;

    guildoptionsEntity = guildEntity.getGuildOptions();

    boolean enabledOption = false;
    if (CommandArray[1].equalsIgnoreCase("enabled")) {
      enabledOption = true;
    }
    switch (CommandArray[0].toLowerCase()) {
      case "customrole":
        {
          guildoptionsEntity.setOption(GuildOptionsEnum.CustomRoleEnabled, enabledOption);
          break;
        }
      case "levelUp":
        {
          guildoptionsEntity.setOption(
              GuildOptionsEnum.LeaderboardLevelUpNotification, enabledOption);
          break;
        }
      case "leaderboards":
        {
          guildoptionsEntity.setOption(GuildOptionsEnum.leaderboardEnabled, enabledOption);
          break;
        }
    }
    displayMenu(event, guildoptionsEntity);

    return guildEntity;
  }

  private void displayMenu(GuildMessageReceivedEvent event, guildOptionsObject guildOptionsEntity) {
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
                guildOptionsEntity.getOption(GuildOptionsEnum.LeaderboardLevelUpNotification)))
        .append("\n");

    embedBuilder.addField("Logging Options", logOptionsMessage.toString(), false);

    String prefix = "!nep " + getCommand();
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
