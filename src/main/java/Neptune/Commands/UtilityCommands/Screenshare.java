package Neptune.Commands.UtilityCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.StorageController;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;

public class Screenshare implements CommandInterface {
    @Override
    public String getName() {
        return "Server Screenshare";
    }

    @Override
    public String getCommand() {
        return "screenshare";
    }

    @Override
    public String getDescription() {
        return "Generates a screenshare url for server voice channels";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.General;
    }

    @Override
    public String getHelp() {
        return null;
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
    public boolean run(MessageReceivedEvent event , StorageController storageController, VariablesStorage variablesStorage, String messageContent) {

        if (event.getMember().getVoiceState().inVoiceChannel()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.addField("Channel", event.getMember().getVoiceState().getChannel().getName(),true);
            embedBuilder.setTitle("Screenshare Link");
            embedBuilder.setColor(Color.MAGENTA);
            embedBuilder.addField("URL","<https://discordapp.com/channels/" + event.getGuild().getId() + "/" + event.getMember().getVoiceState().getChannel().getId() + ">",false);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
        }
        return true;
    }
}
