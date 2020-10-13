package neptune.commands.ImageCommands;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
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


    public anime4k(){
        try {
            FileUtils.deleteDirectory(new File("tmp"));
        } catch (IOException e) {
            log.error(e);
        }
    }
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
                log.trace("Starting upscale pass");
                //upscale pass
                ProcessBuilder pb = new ProcessBuilder();            
                //https://github.com/TianZerL/Anime4KCPP/wiki/CLI
                // --HDN --HDNLevel 3
                String command = "\"" + anime4kPath.getAbsolutePath() + "\" -i \"" + originalImage.getAbsolutePath() + "\" -o \"" + outputImage.getAbsolutePath()+ "\" --CNNMode --GPUMode --alpha --zoomFactor 2  --HDN --HDNLevel 2";
                pb.command(command.split(" "));
                Process p = pb.start();
                p.waitFor();
                log.trace("Starting sharpness pass");
                event.getChannel().sendMessage("Upscale Pass").addFile(outputImage, "output.png").queue();

                //sharpness pass
                originalImage.delete();
                Files.move(outputImage.toPath(), originalImage.toPath());
                Mat source = Imgcodecs.imread(originalImage.getAbsolutePath());
                Mat destination = new Mat();
                if (source.channels() > 3 ){ //preserve alpha if possible
                    Mat sourceNoAlpha = new Mat();
                    Mat destinationNoAlpha = new Mat();
                    List<Mat> colors = new ArrayList<>();
                    Core.split(source, colors);
                    Mat alpha = colors.get(3);
                    colors.remove(3);
                    Core.merge(colors, sourceNoAlpha);
                    Imgproc.GaussianBlur(sourceNoAlpha, destinationNoAlpha, new Size(0,0), 10);
                    Core.addWeighted(sourceNoAlpha, 1.5, destinationNoAlpha, -0.5, 0, destinationNoAlpha);
                    Core.split(destinationNoAlpha, colors);
                    colors.add(3, alpha);
                    Core.merge(colors, destination);
                }
                else{
                    Imgproc.GaussianBlur(source, destination, new Size(0,0), 10);
                    Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
                }
                log.trace("Starting downscale pass");
                //downscale pass
                byte byteImage[] = Mat2byteArray(destination);
                while (byteImage.length > 8388608) {
                    log.trace("Downscaling image");
                    source = destination;
                    Imgproc.resize(source, destination, new Size(0,0), 0.9,0.9,Imgproc.INTER_LINEAR);
                    byteImage = Mat2byteArray(destination);
                }
                //upload to discord
                event.getChannel().sendMessage("Here you go").addFile(byteImage, "output.png").queue();
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

    //https://www.tutorialspoint.com/how-to-convert-opencv-mat-object-to-bufferedimage-object-using-java
    public static byte[] Mat2byteArray(Mat mat) throws IOException{
        //Encoding the image
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", mat, matOfByte);
        return matOfByte.toArray();
    }
}
