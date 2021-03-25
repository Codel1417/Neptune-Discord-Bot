package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.util.Random;

public class CoinFlip implements ICommand {
    Random random = new Random();
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        String coin;
        if (random.nextInt(2) + 1 == 1) {
            coin = "Heads";
        } else coin = "Tails";
        event.getChannel().sendMessage("I flipped a coin and it landed " + coin).queue();
    }
}
