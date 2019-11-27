package neptune.commands.InProgress;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class purgeChannel implements CommandInterface {
    @Override
    public String getName() {
        return "Purge";
    }

    @Override
    public String getCommand() {
        return "purge";
    }

    @Override
    public String getDescription() {
        return "Clears a set number of messages from a channel";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Moderation;
    }

    @Override
    public String getHelp() {
        return getCommand() + " <number of messages>";
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
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        return false;
    }
}
