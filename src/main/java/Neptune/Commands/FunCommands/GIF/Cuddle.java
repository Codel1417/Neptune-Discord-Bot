package Neptune.Commands.FunCommands.GIF;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.TenorGif;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

public class Cuddle extends TenorGif implements CommandInterface {
    @Override
    public String getName() {
        return "Cuddle";
    }

    @Override
    public String getCommand() {
        return "cuddle";
    }

    @Override
    public String getDescription() {
        return "Pictures of cuddles";
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
        return true;    }
}
