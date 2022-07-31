package neptune.commands.FunCommands;

import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Translate implements ISlashCommand {

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.STRING,"text", "Some text to Nep up.",true);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        OptionMapping optionMapping = event.getOption("text");
        String message = optionMapping.getAsString();

        StringBuilder translatedMessage = new StringBuilder();
        for (String string : message.replaceAll("\n", " \n ").split(" ")) {
            String largeWord = "Nepu";
            int translateChangeSize = 6;
            if (string.contains("\n")) {
                translatedMessage.append("\n");
            } else if (string.length() < translateChangeSize) {
                String smallWord = "Nep";
                translatedMessage.append(smallWord).append(" ");
            } else translatedMessage.append(largeWord).append(" ");
        }
        builder.setContent(translatedMessage.toString());
        return builder.build();
    }
}
