package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class PayRespect implements CommandInterface {
    @Override
    public String getName() {
        return "Press F to pay respects";
    }

    @Override
    public String getCommand() {
        return "f";
    }

    @Override
    public String getDescription() {
        return "Pay respect";
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

        event.getChannel()
                .sendMessage(event.getMember().getAsMention() + " has paid respect")
                .queue();
        return guildEntity;
    }
}
