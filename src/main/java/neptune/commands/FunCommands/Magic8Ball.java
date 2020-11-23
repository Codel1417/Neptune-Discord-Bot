package neptune.commands.FunCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.Random;

public class Magic8Ball extends CommonMethods implements CommandInterface {
    @Override
    public String getName() {
        return "Magic 8 Ball";
    }

    @Override
    public String getCommand() {
        return "8ball";
    }

    @Override
    public String getDescription() {
        return "Consult the Magic 8 Ball";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
    }

    @Override
    public String getHelp() {
        return null;
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        String[] Responses = {
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
        Random random = new Random();
        String response = Responses[random.nextInt(Responses.length)];
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle(getName());
        embedBuilder.setColor(Color.BLACK);
        if (messageContent.equalsIgnoreCase("")) {
            embedBuilder.setDescription("Please ask a Yes or No question.");
        } else {
            embedBuilder.setDescription(response);
        }
        event.getChannel().sendMessage(embedBuilder.build()).queue();
        return guildEntity;
    }
}
