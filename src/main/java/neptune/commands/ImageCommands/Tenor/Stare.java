package neptune.commands.ImageCommands.Tenor;

import neptune.commands.CommandInterface;
import neptune.commands.TenorGif;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

public class Stare extends TenorGif implements CommandInterface {
    @Override
    public String getName() {
        return "Stare";
    }

    @Override
    public String getCommand() {
        return "stare";
    }

    @Override
    public String getDescription() {
        return "Pictures of jiiiii";
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
        EmbedBuilder embedBuilder = getImageEmbed(event,"stare anime",true,"Neptune stares at");
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return true;
    }
}
