package neptune.commands.InProgress;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class glsl implements CommandInterface {

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return "glsl";
    }

    @Override
    public String getCommand() {
        // TODO Auto-generated method stub
        return "glsl";
    }

    @Override
    public String getDescription() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public commandCategories getCategory() {
        // TODO Auto-generated method stub
        return commandCategories.Dev;
    }

    @Override
    public String getHelp() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean getRequireManageServer() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean getHideCommand() {
        // TODO Auto-generated method stub
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public guildObject run(GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        // TODO Auto-generated method stub
        return null;
    }
    
}
