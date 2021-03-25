package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Translate implements ICommand {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {

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
        MessageBuilder builder = new MessageBuilder();
        builder.append(translatedMessage.toString());
        event.getChannel().sendMessage(builder.build()).queue();
    }
}
