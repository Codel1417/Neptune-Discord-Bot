package neptune.commands.audio;

import neptune.commands.ICommand;
import neptune.commands.RandomMediaPicker;
import neptune.storage.VariablesStorage;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.io.File;

public class bonk implements ICommand {
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        RandomMediaPicker randomMediaPicker = new RandomMediaPicker();
        VariablesStorage variablesStorage = new VariablesStorage();
        randomMediaPicker.sendMedia(
                new File(
                        variablesStorage.getMediaFolder()
                                + File.separator
                                + "Custom"
                                + File.separator
                                + "bonk"),
                event,
                false,
                true);
    }
}
