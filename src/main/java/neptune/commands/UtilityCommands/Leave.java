package neptune.commands.UtilityCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Leave implements CommandInterface {
    @Override
    public String getName() {
        return "Leave";
    }

    @Override
    public String getCommand() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Disconnect neptune from the Voice Channel";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
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
        if (event.getGuild() != null && event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().setSendingHandler(null);
            event.getGuild().getAudioManager().closeAudioConnection();
            event.getChannel().sendMessage("Neptune has left the Chat!").queue();
            return guildEntity;
        } else {
            event.getChannel().sendMessage("Neptune is not connected to a voice channel").queue();
            return guildEntity;
        }
    }
}
