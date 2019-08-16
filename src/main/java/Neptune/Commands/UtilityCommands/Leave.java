package Neptune.Commands.UtilityCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

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
    public boolean getRequireOwner() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, StorageController storageController, VariablesStorage variablesStorage, String messageContent) {
        if (event.getGuild() != null && event.getGuild().getAudioManager().isConnected()) {
            event.getGuild().getAudioManager().setSendingHandler(null);
            event.getGuild().getAudioManager().closeAudioConnection();
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Left Voice Channel");
            embedBuilder.setDescription("Neptune has left the Chat!");
            embedBuilder.setColor(Color.MAGENTA);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return true;
        }
        else {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle("Unable to leave Voice Channel");
            embedBuilder.setDescription("Neptune is not connected to a voice channel");
            embedBuilder.setColor(Color.RED);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return false;
        }
    }
}
