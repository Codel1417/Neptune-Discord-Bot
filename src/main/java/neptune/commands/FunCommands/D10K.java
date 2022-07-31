package neptune.commands.FunCommands;

import neptune.commands.D10kEntries;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.util.Random;

public class D10K implements ISlashCommand {
    final D10kEntries d10kEntries = new D10kEntries();
    final Random random;

    public D10K() {
        random = new Random();
    }

    @NotNull
    private Message getMessage(MessageBuilder builder, int number, EmbedBuilder embedBuilder) {
        String result;
        if (number == 0) {
            number = random.nextInt(10000) + 1;
        }

        result = d10kEntries.getEntry(number);
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setTitle("D10,000 Random Result");
        embedBuilder.setDescription(result);
        embedBuilder.setFooter("Effect #: " + number, null);
        return builder.setEmbeds(embedBuilder.build()).build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.INTEGER,"entry","Return a specific entry",false);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        int number = 0;
        EmbedBuilder embedBuilder = new EmbedBuilder();
        String result;
        OptionMapping optionMapping = event.getOption("entry");
        try {
            number = (int) optionMapping.getAsLong();
            // prevent numbers above 10,000
            if (number > 10000) {
                number = 0;
            }
        } catch (Exception e) {
            //
        }

        return getMessage(builder, number, embedBuilder);
    }
}
