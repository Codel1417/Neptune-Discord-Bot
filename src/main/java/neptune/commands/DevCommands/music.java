package neptune.commands.DevCommands;

import neptune.commands.ICommand;
import neptune.music.AudioController;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class music implements ICommand {
    private AudioController AudioOut;
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        if (event.getGuild().getAudioManager() != null
                || event.getMember().getVoiceState().getChannel() != null
                        && event.getGuild() != null) {
            AudioOut.playSound(event, messageContent);
        }
    }
}
