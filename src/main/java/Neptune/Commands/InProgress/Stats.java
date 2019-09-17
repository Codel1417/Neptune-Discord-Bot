package Neptune.Commands.InProgress;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.List;

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
        return false;
    }

    @Override
    public boolean getRequireOwner() {
        return true;
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
        List<Guild> guilds = event.getJDA().getShardManager().getGuilds();

        if(guilds != null){

        }

        return false;
    }
}
