package Neptune.Commands;

import Neptune.music.AudioController;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.ArrayList;
import java.util.Random;

public class RandomMediaPicker {
    private final String[] imageType = {"png","jpg","gif"};
    private final String[] audioType = {"wav","ogg"};
    private AudioController AudioOut;
    private ArrayList<File> ImageFiles;
    private ArrayList<File> audioFiles;

    public void sendMedia(File Folder, MessageReceivedEvent event, boolean image, boolean audio){
        if (Folder == null || !Folder.exists() && !Folder.isDirectory()) return; //null check
        System.out.println("Finding Random Media from folder" + Folder.getAbsolutePath());
        searchFolder(Folder, audio, image);
        System.out.println("        Image Files: " + ImageFiles.size());
        System.out.println("        Audio Files: " + audioFiles.size());

        if (event.getGuild() != null && AudioOut == null) {
            AudioOut = new AudioController(event);
        }

        Random rand = new Random();
        if (ImageFiles.size() != 0 && image) {
            File imageFile = ImageFiles.get(rand.nextInt(ImageFiles.size()));
            event.getChannel().sendMessage("\n").addFile(ImageFiles.get(rand.nextInt(ImageFiles.size()))).queue();
        }
        if (audioFiles.size() != 0 && audio) {
            File audioFile = audioFiles.get(rand.nextInt(audioFiles.size()));
            if (event.getGuild().getAudioManager() != null || event.getMember().getVoiceState().getChannel() != null && event.getGuild() != null ) {
                AudioOut.playSound(event, audioFile.getAbsolutePath());
            }
        }
    }
    private void searchFolder(File Folder, boolean audio, boolean image){
        ImageFiles = new ArrayList<>();
        audioFiles = new ArrayList<>();
        for (File file : Folder.listFiles()) {
            String substring = file.getPath().substring(file.getPath().indexOf("."));
            for (String string : audioType){
                if(substring.contains(string)){
                    audioFiles.add(file);
                }
            }
            for  (String string: imageType){
                if(substring.contains(string)){
                    ImageFiles.add(file);
                }
            }
        }
    }
}
