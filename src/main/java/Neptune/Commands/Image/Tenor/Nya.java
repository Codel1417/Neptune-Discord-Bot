package Neptune.Commands.Image.Tenor;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.TenorGif;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Nya extends TenorGif implements CommandInterface {
    @Override
    public String getName() {
        return "Nya";
    }

    @Override
    public String getCommand() {
        return "nya";
    }

    @Override
    public String getDescription() {
        return "Nya >.<";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Image;
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
        EmbedBuilder embedBuilder = getImageEmbed(event,"nya anime",true,"Neptune nya's");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return false;
    }
}
