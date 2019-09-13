package Neptune.Commands.DevCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.apache.commons.io.FileUtils;

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
        embedBuilder.addField("Num of Cores", String.valueOf(Runtime.getRuntime().availableProcessors()),true);
        String usedMem = FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory());
        embedBuilder.addField("Memory used",usedMem + "/"+ FileUtils.byteCountToDisplaySize(Runtime.getRuntime().totalMemory()),true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return false;
    }
}
