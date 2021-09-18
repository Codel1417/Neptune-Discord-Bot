package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Translate implements ICommand, ISlashCommand {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";


    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
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

        StringBuilder translatedMessage = new StringBuilder();
        for (String string : message.replaceAll("\n", " \n ").split(" ")) {
            if (string.contains("\n")) {
                translatedMessage.append("\n");
            } else if (string.length() < TranslateChangeSize) {
                translatedMessage.append(SmallWord).append(" ");
            } else translatedMessage.append(LargeWord).append(" ");
        }
        builder.append(translatedMessage.toString());
        return builder.build();
    }

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
            if (string.contains("\n")) {
                translatedMessage.append("\n");
            } else if (string.length() < TranslateChangeSize) {
                translatedMessage.append(SmallWord).append(" ");
            } else translatedMessage.append(LargeWord).append(" ");
        }
        builder.setContent(translatedMessage.toString());
        return builder.build();
    }
}
