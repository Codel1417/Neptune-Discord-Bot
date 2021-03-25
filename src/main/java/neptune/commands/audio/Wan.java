package neptune.commands.audio;

import neptune.commands.ICommand;
import neptune.commands.RandomMediaPicker;
import neptune.storage.VariablesStorage;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;

public class Wan implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        VariablesStorage variablesStorage = new VariablesStorage();
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
        randomMediaPicker.sendMedia(
                new File(
                        variablesStorage.getMediaFolder()
                                + File.separator
                                + "Custom"
                                + File.separator
                                + "wan"),
                event,
                false,
                true);
    }
}
