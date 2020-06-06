package neptune.commands.AdminCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.MySQL.SettingsStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class LeaderboardOptions extends CommonMethods implements CommandInterface {
    SettingsStorage settingsStorage = new SettingsStorage();

    @Override
    public String getName() {
        return "Leaderboard Options";
    }

    @Override
    public String getCommand() {
        return "leaderboardOptions";
    }

    @Override
    public String getDescription() {
        return "Control Leaderboard settings";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Admin;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean getRequireManageServer() {
        return true;
    }

    @Override
    public boolean getRequireOwner() {
        return false;
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String[] CommandArray = getCommandName(messageContent);
        Map<String, String> options = settingsStorage.getGuildSettings(event.getGuild().getId());
        boolean enabledOption = false;
        if (CommandArray[1].equalsIgnoreCase("enabled")){
            enabledOption = true;
        }
        switch (CommandArray[0].toLowerCase()) {
            case "levelUp":{
                if (enabledOption) {
                    options.put("LeaderboardLevelUpNotificationsEnabled", "enabled");
                } else options.put("LeaderboardLevelUpNotificationsEnabled", "disabled");
                settingsStorage.updateGuild(event.getGuild().getId(), "LeaderboardLevelUpNotificationsEnabled", options.get("LeaderboardLevelUpNotificationsEnabled"));
                break;
            }
            case "leaderboards":{
                if (enabledOption) {
                    options.put("LeaderboardsEnabled", "enabled");
                } else options.put("LeaderboardsEnabled", "disabled");
                settingsStorage.updateGuild(event.getGuild().getId(), "LeaderboardsEnabled", options.get("LeaderboardsEnabled"));
                break;
            }
        }
        displayMenu(event,variablesStorage,options);
        return true;
    }
    private void displayMenu(MessageReceivedEvent event, VariablesStorage variablesStorage, Map<String,String> options){
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("Bot Options");
        embedBuilder.setDescription("Controls Neptune's additional features.");


        StringBuilder logOptionsMessage = new StringBuilder();
        logOptionsMessage.append("Level Up Notifications" ).append(getEnabledDisabledIcon(options.getOrDefault("LeaderboardLevelUpNotificationsEnabled","disabled"))).append("\n");
        logOptionsMessage.append("Leaderboards ").append(getEnabledDisabledIcon(options.getOrDefault("LeaderboardsEnabled","false"))).append("\n");


        embedBuilder.addField("Logging Options",logOptionsMessage.toString(),false);

        String prefix = variablesStorage.getCallBot() + " " + getCommand();
        embedBuilder.addField("Leaderboard Commands Commands","",false);
        embedBuilder.addField("Enable Level Up Notifications",prefix + " levelup <enabled/disabled>",true);
        embedBuilder.addField("Enable Leaderboards",prefix + " leaderboards <enabled/disabled>",true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }





}
