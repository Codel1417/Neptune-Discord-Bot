package Neptune.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.Objects;
import java.util.Random;

public class RandomSoundPicker {
    private AudioController AudioOut;
    public void playRandomAudioFile(MessageReceivedEvent event,File AudioFileFolder) {
        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }
        if (AudioFileFolder.listFiles() != null) {
            Random rand = new Random();
            if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null && event.getGuild() != null ) {
                File audioFile = Objects.requireNonNull(AudioFileFolder.listFiles())[rand.nextInt(AudioFileFolder.listFiles().length)];
                AudioOut.playSound(event, audioFile.getAbsolutePath(), false);
            }
        }
    }
}
