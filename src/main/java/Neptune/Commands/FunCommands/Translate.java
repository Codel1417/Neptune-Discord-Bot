package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;


public class Translate implements CommandInterface {
    private final int TranslateChangeSize = 6;
    private final String SmallWord = "Nep";
    private final String LargeWord = "Nepu";

    @Override
    public String getName() {
        return "Translate";
    }

    @Override
    public String getCommand() {
        return "translate";
    }

    @Override
    public String getDescription() {
        return "Translates the previous message into Nep";
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
    public boolean getRequireOwner() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
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
        return true;
    }
}
