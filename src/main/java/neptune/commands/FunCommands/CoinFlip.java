package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Random;

public class CoinFlip implements ICommand, ISlashCommand {
    final Random random = new Random();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        String coin;
        if (random.nextInt(2) + 1 == 1) {
            coin = "Heads";
        } else coin = "Tails";
        return builder.setContent("I flipped a coin and it landed " + coin).build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        String coin;
        if (random.nextInt(2) + 1 == 1) {
            coin = "Heads";
        } else coin = "Tails";
        return builder.setContent("I flipped a coin and it landed " + coin).build();
    }
}
