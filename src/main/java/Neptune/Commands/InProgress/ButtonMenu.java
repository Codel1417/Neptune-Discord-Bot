package Neptune.Commands.InProgress;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import com.jagrosh.jdautilities.commons.waiter.EventWaiter;
import com.jagrosh.jdautilities.menu.Menu;
import com.jagrosh.jdautilities.menu.SelectionDialog;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class ButtonMenu implements CommandInterface {
    EventWaiter eventWaiter = new EventWaiter();
    @Override
    public String getName() {
        return "";
    }

    @Override
    public String getCommand() {
        return "button";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Dev;
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
        return true;
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
        SelectionDialog.Builder builder = new SelectionDialog.Builder();
        builder.addChoices("Choice 1");
        builder.build();
        builder.build().display(event.getChannel());


        return false;
    }
}
