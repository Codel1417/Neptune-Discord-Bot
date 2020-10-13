package neptune.commands.InProgress;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.concurrent.ExecutionException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message.Attachment;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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
        nu.pattern.OpenCV.loadLocally();

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
            //https://github.com/TianZerL/Anime4KCPP/wiki/CLI
            //--postprocessing --postFilters 48
            // --HDN --HDNLevel 3
            String command = "\"" + anime4kPath.getAbsolutePath() + "\" -i \"" + originalImage.getAbsolutePath() + "\" -o \"" + outputImage.getAbsolutePath()+ "\" --CNNMode --GPUMode --alpha --zoomFactor 4";
            pb.command(command.split(" "));
            Process p = pb.start();
            exitcode = p.waitFor();

        } catch (Exception e) {
            log.error(e);
            return guildEntity;
        }

        //sharpness pass
        try {
            originalImage.delete();
            Files.move(outputImage.toPath(), originalImage.toPath());
        } catch (IOException e1) {
            log.error(e1);
            return guildEntity;
        }
        Mat source = Imgcodecs.imread(originalImage.getAbsolutePath());
        Mat destination = Imgcodecs.imread(outputImage.getAbsolutePath());
        Imgproc.GaussianBlur(source, destination, new Size(0,0), 10);
        Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
        Imgcodecs.imwrite(outputImage.getAbsolutePath(), destination);

        //downscale pass
        BufferedImage img;
        while (outputImage.length() > 8388608)
        log.warn("Downscaling image");
        try {
            img = ImageIO.read(outputImage);
            img = scale(img, (int)(img.getWidth() * 0.95), (int)(img.getHeight() * 0.95));
            ImageIO.write(img, "png", outputImage);

        } catch (IOException e1) {
            log.error(e1);
        }

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
    static BufferedImage scale(BufferedImage bi, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)newImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(bi, 0, 0, width, height, null);
        return newImage;
    }
    
}
