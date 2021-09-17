package neptune.commands.DevCommands;

import neptune.commands.ICommand;
import neptune.music.AudioController;

import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class music implements ICommand {
    private AudioController AudioOut;
    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        if (AudioOut == null) {
            AudioOut = new AudioController(event.getGuild());
        }
        AudioOut.playSound(event, messageContent);
        return builder.setContent("Attempting to play: " + messageContent).build();
    }
}
