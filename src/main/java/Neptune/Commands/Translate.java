package Neptune.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


class Translate {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";

    void TranslateString(MessageReceivedEvent event) {
        StringBuilder translatedMessage = new StringBuilder();
        for (String string: event.getChannel().getHistory().retrievePast(2).complete().get(1).getContentDisplay().replaceAll("\n"," \n ").split(" ")) {
            if (string.contains("\n")) {
                translatedMessage.append("\n");
            }
            else if (string.length() < TranslateChangeSize) {
                translatedMessage.append(SmallWord).append(" ");
            }
            else translatedMessage.append(LargeWord).append(" ");
        }
        event.getChannel().sendMessage(translatedMessage.toString()).queue();
    }
}
