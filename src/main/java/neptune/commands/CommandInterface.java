package neptune.commands;

import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public interface CommandInterface {
    String getName();
    String getCommand();
    String getDescription();
    commandCategories getCategory();
    String getHelp();
    boolean getRequireManageServer();
    boolean getHideCommand();
    boolean getRequireManageUsers();
    guildObject run(GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity);

}
