package neptune.commands.FunCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import java.awt.*;
import java.util.Random;

public class Magic8Ball extends Helpers implements ICommand {
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
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {

        Random random = new Random();
        String response = Responses[random.nextInt(Responses.length)];
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Magic 8 Ball");
        embedBuilder.setColor(Color.BLACK);
        if (messageContent.equalsIgnoreCase("")) {
            embedBuilder.setDescription("Please ask a Yes or No question.");
        } else {
            embedBuilder.setDescription(response);
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
