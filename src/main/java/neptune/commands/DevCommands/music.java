package neptune.commands.DevCommands;

import neptune.commands.ICommand;
import neptune.music.AudioController;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class music implements ICommand {
    private AudioController AudioOut;
    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        event.getGuild();
        if (AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        if (event.getGuild().getAudioManager() == null) {
            if (event.getMember().getVoiceState().getChannel() != null) {
                event.getGuild();
            }
        }
        AudioOut.playSound(event, messageContent);
    }
}
