package neptune.commands.DevCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;

public class ServerInfo implements CommandInterface {
    @Override
    public String getName() {
        return "Server Info";
    }

    @Override
    public String getCommand() {
        return "serverinfo";
    }

    @Override
    public String getDescription() {
        return "Some stats from the server hosting Neptune";
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
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        embedBuilder.setColor(Color.MAGENTA);
        StringBuilder stringBuilder = new StringBuilder();
        embedBuilder.setDescription(stringBuilder.toString());
        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return false;
    }
}
