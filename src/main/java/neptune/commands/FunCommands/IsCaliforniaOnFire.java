package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class IsCaliforniaOnFire implements CommandInterface {
    @Override
    public String getName() {
        return "Is California on Fire?";
    }

    @Override
    public String getCommand() {
        return "iscaliforniaonfire";
    }

    @Override
    public String getDescription() {
        return "Ever wonder if california is on fire?";
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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        event.getChannel().sendMessage("Yes").queue();
        return true;
    }
}
