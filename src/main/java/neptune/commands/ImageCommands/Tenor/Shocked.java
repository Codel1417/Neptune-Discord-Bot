package neptune.commands.ImageCommands.Tenor;

import neptune.commands.ICommand;
import neptune.commands.TenorGif;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class Shocked extends TenorGif implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        EmbedBuilder embedBuilder = getImageDefaultEmbed(event, "shocked", false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }
}
