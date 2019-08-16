package Neptune.Commands;

import Neptune.music.AudioController;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;
import java.util.Random;

public class RandomMediaPicker {
    private final String[] imageType = {"png","jpg","gif"};
    private final String[] audioType = {"wav","ogg"};
    private AudioController AudioOut;
    private ArrayList<File> ImageFiles;
    private ArrayList<File> audioFiles;

    public void sendMedia(File Folder, MessageReceivedEvent event, boolean image, boolean audio){
        if (Folder == null || !Folder.exists() && !Folder.isDirectory()) return; //null check
        searchFolder(Folder, audio, image);

        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }

        Random rand = new Random();
        if (ImageFiles.size() != 0 && image) {
            event.getChannel().sendMessage("\n").addFile(ImageFiles.get(rand.nextInt(ImageFiles.size()))).queue();
        }
        if (audioFiles.size() != 0 && audio) {
            if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null && event.getGuild() != null ) {
                File audioFile = audioFiles.get(rand.nextInt(audioFiles.size()));
                AudioOut.playSound(event, audioFile.getAbsolutePath());
            }
        }
    }
    private void searchFolder(File Folder, boolean audio, boolean image){
        ImageFiles = new ArrayList<>();
        audioFiles = new ArrayList<>();
        for (File file : Folder.listFiles()) {
            String substring = file.getPath().substring(file.getPath().indexOf("."));
            if(Arrays.asList(audioType).contains(substring)){
                audioFiles.add(file);
            }
            if(Arrays.asList(imageType).contains(substring)){
                ImageFiles.add(file);
            }
        }
    }
}
