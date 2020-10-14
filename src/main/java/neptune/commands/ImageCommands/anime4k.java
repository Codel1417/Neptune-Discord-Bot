package neptune.commands.ImageCommands;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgcodecs.Imgcodecs;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.entities.Message.Attachment;

public class anime4k implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();
    File anime4kPath = new File("Anime4KCPP_CLI" + File.separator + "Anime4KCPP_CLI.exe");

    public anime4k() {
        try {
                FileUtils.deleteDirectory(new File("tmp")); 
            } 
                catch (IOException e) {
                log.error(e);
            }
    }
    @Override
    public String getName() {
        return "Anime4k Image Upscaling";
    }
    
    @Override
    public String getCommand() {
        return "anime4k";
    }
    
    @Override
    public String getDescription() {
        return "Upscales images using anime4k, then sharpens them";
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
        nu.pattern.OpenCV.loadLocally(); //load opencv library natives

        File directory = new File("tmp" + File.separator + event.getMessageId() + File.separator);
        File originalImage;
        File outputImage;
        try {
            directory.mkdirs();
            List<Attachment> attachments = event.getMessage().getAttachments();
            if (!attachments.isEmpty()) {
                Attachment image = attachments.get(0);
                originalImage = new File(directory, "original." + image.getFileExtension());
                outputImage = new File(directory, "output." + image.getFileExtension());
                image.downloadToFile(originalImage).get();
                log.warn("Starting upscale pass");
                
                //upscale pass
                ProcessBuilder pb = new ProcessBuilder();            
                //https://github.com/TianZerL/Anime4KCPP/wiki/CLI
                String command = "\"" + anime4kPath.getAbsolutePath() + "\" -i \"" + originalImage.getAbsolutePath() + "\" -o \"" + outputImage.getAbsolutePath()+ "\" --CNNMode --GPUMode --alpha --zoomFactor 4  --HDN --HDNLevel 3";
                pb.command(command.split(" "));

                pb.inheritIO();
                Process p = pb.start();
                int exitcode = p.waitFor();
                if (exitcode != 0){
                    throw new FileNotFoundException("Upscaling Process Failed");
                }


                log.warn("Starting sharpness pass");
                //sharpness pass
                Mat source = Imgcodecs.imread(outputImage.getAbsolutePath());
                Mat destination = new Mat();

                Mat sourceNoAlpha = new Mat();
                Mat destinationNoAlpha = new Mat();

                destination.copySize(source);
                sourceNoAlpha.copySize(source);
                destinationNoAlpha.copySize(source);
                source.convertTo(source, CvType.makeType(source.depth(), 4));
                List<Mat> colors = new ArrayList<>(4);
                Core.split(source, colors);
                Mat alpha = colors.get(3);
                colors.remove(3);
                Core.merge(colors, sourceNoAlpha);
                alpha.convertTo(alpha, CvType.makeType(source.depth(), 4));
                sourceNoAlpha.convertTo(sourceNoAlpha, CvType.makeType(source.depth(), 4));


                Imgproc.GaussianBlur(sourceNoAlpha, destinationNoAlpha, new Size(0,0), 10);
                Core.addWeighted(sourceNoAlpha, 1.5, destinationNoAlpha, -0.5, 0, destinationNoAlpha);
                colors = new ArrayList<>(4);
                Core.split(destinationNoAlpha, colors);
                colors.add(3, alpha);
                Core.merge(colors, destination);
                destination.convertTo(destination, CvType.makeType(source.depth(), 4));

                log.warn("Starting downscale pass");
                //downscale pass
                byte byteImage[] = Mat2byteArray(destination);
                while (byteImage.length > 8388608) {
                    log.warn("Downscaling image");
                    source = destination;
                    Imgproc.resize(source, destination, new Size(0,0), 0.9,0.9,Imgproc.INTER_LINEAR);
                    byteImage = Mat2byteArray(destination);
                }
                //upload to discord
                //event.getChannel().sendMessage("Upscale Pass").addFile(outputImage, "output.png").complete();
                event.getChannel().sendMessage("Here you go").addFile(byteImage, "output.png").complete();
            }
        } catch (Exception e) {
            log.error(e);
        }
        finally {
            //clean up directory
            try {
                FileUtils.deleteDirectory(directory);
            } catch (Exception e) {
                log.error(e);
            }
    }
    return guildEntity;
}

  // https://www.tutorialspoint.com/how-to-convert-opencv-mat-object-to-bufferedimage-object-using-java
    public static byte[] Mat2byteArray(Mat mat) throws IOException {
        // Encoding the image
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return matOfByte.toArray();
    }
}
