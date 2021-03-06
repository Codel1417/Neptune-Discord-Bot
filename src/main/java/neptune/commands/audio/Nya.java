package neptune.commands.audio;

import neptune.commands.ICommand;
import neptune.commands.RandomMediaPicker;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;

public class Nya implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
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
    }
}
