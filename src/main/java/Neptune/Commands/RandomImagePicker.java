package Neptune.Commands;

import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.io.File;
import java.util.Random;

public class RandomImagePicker {

    public void pickImage(String Folder, MessageReceivedEvent event){
        File folder = new File(Folder);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            Random rand = new Random();
            event.getChannel().sendMessage("\n").addFile(listOfFiles[rand.nextInt(listOfFiles.length)]).queue();

        }
    }
}
