package neptune.commands.ImageCommands.Tenor;

import neptune.commands.ISlashCommand;
import neptune.commands.ImageCommands.TenorGif;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class Senko extends TenorGif implements ISlashCommand {
    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return null;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        EmbedBuilder embedBuilder = getImageEmbed("senko-san", false, null);
        return builder.setEmbeds(embedBuilder.build()).build();
    }
}
