package Neptune.Commands;

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
    boolean getHideCommand();
    boolean getRequireManageUsers();
    boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent);

}
