package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.HashMap;
import java.util.Map;

public class UWU_Translater implements CommandInterface {
    private HashMap<String, String> directTranslations = new HashMap<>();
    private HashMap<String, String> indirectTranslations = new HashMap<>();

    public UWU_Translater() {
        // init maps
        indirectTranslations.put("R", "W");
        indirectTranslations.put("r", "w");
        indirectTranslations.put("L", "W");
        indirectTranslations.put("l", "w");
        indirectTranslations.put("V", "B");
        indirectTranslations.put("v", "b");
        indirectTranslations.put("ove", "uv");
        indirectTranslations.put("what", "whawt");
        indirectTranslations.put("You", "U");
        indirectTranslations.put("you", "u");
        indirectTranslations.put("to", "tuwu");
        indirectTranslations.put("on", "own");
        indirectTranslations.put("and", "awnd");
        indirectTranslations.put("both", "bod");
        indirectTranslations.put("Both", "Bod");
        indirectTranslations.put("the", "de");
        indirectTranslations.put("The", "De");
        indirectTranslations.put("One", "Wan");
        indirectTranslations.put("one", "wan");
        indirectTranslations.put("th", "f");
        indirectTranslations.put("Th", "f");
    }

    @Override
    public String getName() {
        return "UwU Translator";
    }

    @Override
    public String getCommand() {
        return "uwu";
    }

    @Override
    public String getDescription() {
        return "Translates the previous message from english to UwU";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
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
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        String message;
        if (messageContent.equalsIgnoreCase("")) {
            message =
                    event.getChannel()
                            .getHistory()
                            .retrievePast(2)
                            .complete()
                            .get(1)
                            .getContentDisplay()
                            .replaceAll("\n", " \n ");
        } else {
            message = messageContent;
        }
        StringBuilder result = new StringBuilder();
        if (message.length() != 0) {
            for (String word : message.split(" ")) {
                if (word.matches("\n")) {
                    result.append("\n");
                } else if (directTranslations.containsKey(word)) {
                    result.append(directTranslations.get(word)).append(" ");
                } else {
                    for (Map.Entry<String, String> partial : indirectTranslations.entrySet()) {
                        if (word.toLowerCase().contains(partial.getKey().toLowerCase())) {
                            word = word.replaceAll(partial.getKey(), partial.getValue());
                        }
                    }
                    result.append(word).append(" ");
                }
            }

            MessageBuilder builder = new MessageBuilder();
            builder.append(result.toString());
            event.getChannel().sendMessage(builder.build()).queue();
        }
        return guildEntity;
    }
}
