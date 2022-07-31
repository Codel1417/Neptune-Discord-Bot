package neptune.commands.FunCommands;

import neptune.commands.ISlashCommand;
import neptune.commands.RandomMediaPicker;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.util.Random;

public class Attack implements ISlashCommand {
    final Random random;
    RandomMediaPicker randomMediaPicker = new RandomMediaPicker();

    public Attack() {
        random = new Random();
    }


    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.USER,"target","Who do you want to attack?",true);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        OptionMapping optionMapping = event.getOption("target");
        User mention = optionMapping.getAsUser();

        stringBuilder.append("Neptune attacked ");
        stringBuilder.append(mention.getAsMention()).append(" ");
        stringBuilder.append("for ").append(random.nextInt(6)).append(" damage");
        return builder.setContent(stringBuilder.toString()).build();
    }
}
