package neptune.commands.audio;

import neptune.commands.ISlashCommand;
import neptune.commands.RandomMediaPicker;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;

import java.io.File;

public class Nya implements  ISlashCommand {
    final RandomMediaPicker randomMediaPicker = new RandomMediaPicker();

    @Override
    public CommandData RegisterCommand(CommandData commandData) {
        return null;
    }

    @Override
    public Message run(SlashCommandEvent event, MessageBuilder builder) {
        randomMediaPicker.sendMedia(
                new File(
                        "Media"
                                + File.separator
                                + "Custom"
                                + File.separator
                                + "nya"),
                event,
                false,
                true);
        return builder.setContent("Nya~").build();
    }
}
