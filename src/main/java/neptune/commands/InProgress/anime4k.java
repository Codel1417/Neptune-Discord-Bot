package neptune.commands.InProgress;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message.Attachment;

public class anime4k implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();
    File anime4kPath = new File("Anime4KCPP_CLI" + File.separator + "Anime4KCPP_CLI.exe");

    @Override
    public String getName() {
        return "anime4k image upscaling";
    }

    @Override
    public String getCommand() {
        return "anime4k";
    }

    @Override
    public String getDescription() {
        return "Upscales and enhances an attached 2d image";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Image;
    }

    @Override
    public String getHelp() {
        return "Run this command with an image attached";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return false;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        File directory = new File("tmp" + File.separator + event.getMessageId() + File.separator);
        File originalImage;
        File outputImage;
        directory.mkdirs();
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty()) {
            Attachment image = attachments.get(0);
            originalImage = new File(directory, "original." + image.getFileExtension());
            outputImage = new File(directory, "output." + image.getFileExtension());
            try {
                image.downloadToFile(originalImage).get();
            } catch (InterruptedException | ExecutionException e) {
                log.error(e);
                return guildEntity;
            }
        }
        else return guildEntity;
        int exitcode;
        try {
            ProcessBuilder pb = new ProcessBuilder();
            String command = "\"" + anime4kPath.getAbsolutePath() + "\" -i \"" + originalImage.getAbsolutePath() + "\" -o \"" + outputImage.getAbsolutePath()+ "\" --CNNMode --GPUMode --HDN --HDNLevel 1 --alpha --postprocessing --postFilters 48";
            pb.command(command.split(" "));
            Process p = pb.start();
            exitcode = p.waitFor();

        } catch (Exception e) {
            log.error(e);
            return guildEntity;
        }
        //todo: Downscale images that exceed 8mb limit
        if (outputImage.exists() &&  exitcode == 0){
            event.getChannel().sendFile(outputImage).complete();
        }
        try {
            FileUtils.deleteDirectory(directory);
        } catch (IOException e) {
            log.error(e);
        }
        return guildEntity; 
    }
    
}
