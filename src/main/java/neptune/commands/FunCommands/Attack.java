package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.ISlashCommand;
import neptune.commands.RandomMediaPicker;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;
import java.util.List;
import java.util.Random;

public class Attack implements ICommand, ISlashCommand {
    Random random = new Random();
    RandomMediaPicker randomMediaPicker = new RandomMediaPicker();


    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        StringBuilder stringBuilder = new StringBuilder();
        List<Member> mention = event.getMessage().getMentionedMembers();
        stringBuilder.append("Neptune attacked ");
        if (mention.size() != 0) {
            Member target = mention.get(random.nextInt(mention.size()));
            stringBuilder.append(target.getAsMention()).append(" ");
        }
        stringBuilder.append("for ").append(random.nextInt(6)).append(" damage");
        randomMediaPicker.sendMedia(
                new File("Media" + File.separator + "Attack"),
                event,
                false,
                true);
        return builder.setContent(stringBuilder.toString()).build();
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
