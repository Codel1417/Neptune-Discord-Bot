package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.util.Random;

public class CoinFlip implements CommandInterface {
    Random random = new Random();
    @Override
    public String getName() {
        return "Flip a Coin";
    }

    @Override
    public String getCommand() {
        return "flip";
    }

    @Override
    public String getDescription() {
        return "Flips a coin";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
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
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String coin;
        if (random.nextInt(2) + 1 == 1){
            coin = "Heads";
        }
        else coin = "Tails";

        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        embedBuilder.setDescription("I flipped a coin and it landed " + coin);
        event.getChannel().sendMessage(embedBuilder.build()).queue();

        return true;
    }
}
