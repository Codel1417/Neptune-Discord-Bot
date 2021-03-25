package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.CommandHelpers;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.util.Random;

public class RollDie extends CommandHelpers implements ICommand {
    Random random = new Random();

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        int sides = Integer.decode(messageContent);
        int result = random.nextInt(sides) + 1;
        event.getChannel().sendMessage("I rolled a d" + sides + " and landed a " + result).queue();
    }
}
