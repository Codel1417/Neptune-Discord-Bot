package Neptune.Commands.InProgress;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Stats implements CommandInterface {
    @Override
    public String getName() {
        return "Stats";
    }

    @Override
    public String getCommand() {
        return "stats";
    }

    @Override
    public String getDescription() {
        return "";
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
        return true;
    }

    @Override
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        return false;
    }
}
