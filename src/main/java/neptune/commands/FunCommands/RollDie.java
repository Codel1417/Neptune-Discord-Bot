package neptune.commands.FunCommands;

import neptune.commands.Helpers;

import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Random;

public class RollDie extends Helpers implements ISlashCommand {
    final Random random;

    public RollDie() {
        random = new Random();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.INTEGER,"sides", "The number of sides on the die.",true);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        int sides;
        OptionMapping optionMapping = event.getOption("sides");
        try {
            sides = (int) optionMapping.getAsLong();
        }
        catch (NumberFormatException | NullPointerException e){
            builder.setContent(optionMapping.getAsString() + " is not aa number");
            return builder.build();
        }
        int result = random.nextInt(sides) + 1;
        builder.setContent("I rolled a d" + sides + " and landed a " + result);
        return builder.build();    }
}
