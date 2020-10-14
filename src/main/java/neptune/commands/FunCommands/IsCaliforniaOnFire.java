package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class IsCaliforniaOnFire implements CommandInterface {
    @Override
    public String getName() {
        return "Is California on Fire?";
    }

    @Override
    public String getCommand() {
        return "isCaliforniaOnFire";
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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        event.getChannel().sendMessage("Yes").queue();
        return guildEntity;
    }
}
