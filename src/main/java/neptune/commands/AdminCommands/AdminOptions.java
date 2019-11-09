package neptune.commands.AdminCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.SQLite.SettingsStorage;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.util.Map;

public class AdminOptions extends CommonMethods implements CommandInterface {
SettingsStorage settingsStorage = new SettingsStorage();
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
            case "tts": {
                if (enabledOption) {
                    options.put("TTS", "enabled");
                } else {
                    options.put("TTS", "disabled");

                }
                settingsStorage.updateGuild(event.getGuild().getId(), "TTS", options.get("TTS"));
                break;
            }
            case "customsounds": {
                if (enabledOption) {
                    options.put("CustomSounds", "enabled");
                } else options.put("CustomSounds", "disabled");
                settingsStorage.updateGuild(event.getGuild().getId(), "CustomSounds", options.get("CustomSounds"));
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
        logOptionsMessage.append("Use TTS ").append(getEnabledDisabledIcon(options.getOrDefault("TTS","disabled"))).append("\n");
        logOptionsMessage.append("Custom Media Commands" ).append(getEnabledDisabledIcon(options.getOrDefault("CustomSounds","disabled"))).append("\n");
        embedBuilder.addField("Logging Options",logOptionsMessage.toString(),false);

        String prefix = variablesStorage.getCallBot() + " " + getCommand();
        embedBuilder.addField("Admin Commands","",false);
        embedBuilder.addField("Enable TTS usage",prefix + " tts <enabled/disabled>",true);
        embedBuilder.addField("Enable Additional Media commands",prefix + " CustomSounds <enabled/disabled>",true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();

    }

}

