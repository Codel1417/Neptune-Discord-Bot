package neptune.commands.ImageCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class anime4k implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();
    File anime4kPath = new File("dependentcies" + File.separator + "Anime4KCPP" + File.separator + "Anime4KCPP_CLI");
    CommonMethods helper = new CommonMethods();

    public anime4k() {
        try {
            CommonMethods.deleteDirectory(new File("tmp"));
        } catch (IOException e) {
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
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        nu.pattern.OpenCV.loadLocally(); // load opencv library natives

        File directory = new File("tmp" + File.separator + event.getMessageId() + File.separator);
        File originalImage;
        File outputImage;
        try {
            directory.mkdirs();
            String url = helper.getImageUrl(event);

            if (url != null) {
                originalImage = new File(directory, "original.png");
                outputImage = new File(directory, "output.png");
                URL urls = new URL(url);
                BufferedImage image = ImageIO.read(urls);
                ImageIO.write(image, "png", originalImage);

                log.trace("Starting upscale pass");

                // upscale pass
                ProcessBuilder pb = new ProcessBuilder();
                // https://github.com/TianZerL/Anime4KCPP/wiki/CLI
                String command =
                        "\""
                                + anime4kPath.getAbsolutePath()
                                + "\" -i \""
                                + originalImage.getAbsolutePath()
                                + "\" -o \""
                                + outputImage.getAbsolutePath()
                                + "\" --CNNMode --alpha --zoomFactor 4  --HDN --HDNLevel"
                                + " 2";
                pb.command(command.split(" "));

                Process p = pb.start();
                int exitcode = p.waitFor();
                if (exitcode != 0) {
                    throw new FileNotFoundException("Upscaling Process Failed");
                }

                log.trace("Starting sharpness pass");
                // sharpness pass
                Mat source =
                        Imgcodecs.imread(outputImage.getAbsolutePath(), Imgcodecs.IMREAD_UNCHANGED);
                Mat destination = new Mat();

                // GaussianBlur does not work with alpha channel. So we remove the alpha channel,
                // apply GaussianBlur, then reassemble the image.
                if (source.channels() > 3) {
                    Mat sourceNoAlpha = new Mat();
                    Mat destinationNoAlpha = new Mat();
                    ArrayList<Mat> colors = new ArrayList<>();
                    Core.split(source, colors);
                    Mat alpha = colors.get(colors.size() - 1); // assumed last channel is alpha
                    colors.remove(alpha);
                    Core.merge(colors, sourceNoAlpha);
                    Imgproc.GaussianBlur(sourceNoAlpha, destinationNoAlpha, new Size(0, 0), 10);
                    Core.addWeighted(
                            sourceNoAlpha, 1.5, destinationNoAlpha, -0.5, 0, destinationNoAlpha);
                    colors.clear();
                    Core.split(destinationNoAlpha, colors);
                    colors.add(alpha);
                    Core.merge(colors, destination);
                } else {
                    Imgproc.GaussianBlur(source, destination, new Size(0, 0), 10);
                    Core.addWeighted(source, 1.5, destination, -0.5, 0, destination);
                }

                log.trace("Starting downscale pass");
                // downscale pass
                byte byteImage[] = Mat2byteArray(destination);
                while (byteImage.length > 8388608) {
                    log.trace("Downscaling image");
                    destination.copyTo(source);
                    Imgproc.resize(
                            source, destination, new Size(0, 0), 0.9, 0.9, Imgproc.INTER_LINEAR);
                    byteImage = Mat2byteArray(destination);
                }
                // upload to discord
                event.getChannel()
                        .sendMessage("Here you go")
                        .addFile(byteImage, "output.webp")
                        .complete();
            }
        } catch (Exception e) {
            log.error(e);
        } finally {
            // clean up directory
            try {
                CommonMethods.deleteDirectory(directory);
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
        Imgcodecs.imencode(".webp", mat, matOfByte);

        return matOfByte.toArray();
    }
}
