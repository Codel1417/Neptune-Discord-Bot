package neptune.commands.UtilityCommands;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.Color;
import java.time.Instant;

public class unixTime implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        Instant instant = Instant.now();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Unix Time");
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.addField("Unix Time", String.valueOf(instant.getEpochSecond()), true);
        embedBuilder.addField("UTC Time", instant.toString(), true);

        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
