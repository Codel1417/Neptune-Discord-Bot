package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

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
        String coin;
        if (random.nextInt(2) + 1 == 1) {
            coin = "Heads";
        } else coin = "Tails";
        event.getChannel().sendMessage("I flipped a coin and it landed " + coin).queue();

        return guildEntity;
    }
}
