package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;

import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Random;

public class RollDie extends Helpers implements ICommand, ISlashCommand {
    final Random random = new Random();


    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        int sides;
        try {
            sides = Integer.decode(messageContent);
        }
        catch (NumberFormatException e){
            builder.setContent(messageContent + " is not aa number");
            return builder.build();
        }
        int result = random.nextInt(sides) + 1;

        builder.setContent("I rolled a d" + sides + " and landed a " + result);
        return builder.build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.INTEGER,"Sides", "The number of sides on the die.");
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        int sides;
        OptionMapping optionMapping = event.getOption("Sides");
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
