package Neptune.Commands.FunCommands.GIF;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.TenorGif;
import Neptune.Commands.commandCategories;
import Neptune.Storage.GetAuthToken;
import Neptune.Storage.TenorConnection;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public class Pat extends TenorGif implements CommandInterface {

    @Override
    public String getName() {
        return "pat";
    }

    @Override
    public String getCommand() {
        return "pat";
    }

    @Override
    public String getDescription() {
        return "Pictures of Anime Pats";
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
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        EmbedBuilder embedBuilder = getImageEmbed(event,getCommand());
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
