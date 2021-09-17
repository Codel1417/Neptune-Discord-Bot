package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import neptune.commands.ISlashCommand;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.awt.*;
import java.util.Random;

public class Magic8Ball extends Helpers implements ICommand, ISlashCommand {
    Random random = new Random();

    final String[] Responses = {
        "It is certain.",
        "It is decidedly so.",
        "Without a doubt.",
        "Yes – definitely.",
        "You may rely on it.",
        "As I see it, yes.",
        "Most likely.",
        "Outlook good.",
        "Yes.",
        "Signs point to yes.",
        "Reply hazy, try again.",
        "Ask again later.",
        "Better not tell you now.",
        "Cannot predict now.",
        "Concentrate and ask again.",
        "Don't count on it.",
        "My reply is no.",
        "My sources say no.",
        "Outlook not so good.",
        "Very doubtful."
    };

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        String response = Responses[random.nextInt(Responses.length)];
        if (messageContent.equalsIgnoreCase("")) {
            response = "Please ask a Yes or No question.";
        }
        return builder.setContent(response).build();
    }

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return commandData.addOption(OptionType.STRING,"Question","Please ask a yes or no question",true);
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        String response = Responses[random.nextInt(Responses.length)];
        return builder.setContent(response).build();    }
}
