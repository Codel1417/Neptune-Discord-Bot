package neptune.commands;

import neptune.music.AudioController;

import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;

public class RandomMediaPicker {
    private final String[] imageType = {"png", "jpg", "gif"};
    private final String[] audioType = {"wav", "ogg"};
    private AudioController AudioOut;
    private ArrayList<File> ImageFiles;
    private ArrayList<File> audioFiles;
    protected static final Logger log = LogManager.getLogger();
    final Random rand = new Random();

    public void sendMedia(File Folder, GuildMessageReceivedEvent event, boolean image, boolean audio) {
        if (Folder == null || !Folder.exists() && !Folder.isDirectory()) return; // null check
        log.trace("Finding Random Media from folder" + Folder.getAbsolutePath());
        searchFolder(Folder);
        log.trace("Image Files: " + ImageFiles.size() + " Audio Files: " + audioFiles.size());

        if (AudioOut == null) {
            AudioOut = new AudioController(event.getGuild());
        }

        if (audioFiles.size() != 0 && audio) {
            File audioFile = audioFiles.get(rand.nextInt(audioFiles.size()));
            AudioOut.playSound(event, audioFile.getAbsolutePath());
        }
        if (ImageFiles.size() != 0 && image) {
            rand.nextInt(ImageFiles.size());
        }
    }
    public void sendMedia(File Folder, SlashCommandEvent event, boolean image, boolean audio) {
        if (Folder == null || !Folder.exists() && !Folder.isDirectory()) return; // null check
        log.trace("Finding Random Media from folder" + Folder.getAbsolutePath());
        searchFolder(Folder);
        log.trace("Image Files: " + ImageFiles.size() + " Audio Files: " + audioFiles.size());

        if (AudioOut == null) {
            AudioOut = new AudioController(event.getGuild());
        }

        if (audioFiles.size() != 0 && audio) {
            File audioFile = audioFiles.get(rand.nextInt(audioFiles.size()));
            AudioOut.playSound(event, audioFile.getAbsolutePath());
        }
        if (ImageFiles.size() != 0 && image) {
            rand.nextInt(ImageFiles.size());
        }
    }
    private void searchFolder(File Folder) {
        ImageFiles = new ArrayList<>();
        audioFiles = new ArrayList<>();
        for (File file : Objects.requireNonNull(Folder.listFiles())) {
            String substring = file.getPath().substring(file.getPath().indexOf("."));
            for (String string : audioType) {
                if (substring.contains(string)) {
                    audioFiles.add(file);
                }
            }
            for (String string : imageType) {
                if (substring.contains(string)) {
                    ImageFiles.add(file);
                }
            }
        }
    }
}
