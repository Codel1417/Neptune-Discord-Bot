package Neptune.Commands;

import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public interface CommandInterface {
    String getName();
    String getCommand();
    String getDescription();
    commandCategories getCategory();
    String getHelp();
    boolean getRequireManageServer();
    boolean getRequireOwner();
    boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent);

}
