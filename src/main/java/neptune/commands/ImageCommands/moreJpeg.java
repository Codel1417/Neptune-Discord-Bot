package neptune.commands.ImageCommands;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class moreJpeg implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();

    @Override
    public String getName() {
        return "jpeg";
    }

    @Override
    public String getCommand() {
        return "jpeg";
    }

    @Override
    public String getDescription() {
        return "Adds more jpeg to an attached image";
    }

    @Override
    public commandCategories getCategory() {
        // TODO Auto-generated method stub
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
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty()) {
            Attachment image = attachments.get(0);
            BufferedImage img;

            // This attempts to get the final image url after redirects
            HttpURLConnection connection;
            String finalUrl = image.getUrl();
            try {
                do {
                    connection = (HttpURLConnection) new URL(finalUrl).openConnection();
                    connection.setInstanceFollowRedirects(false);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode >= 300 && responseCode < 400) {
                        String redirectedUrl = connection.getHeaderField("Location");
                        if (null == redirectedUrl) break;
                        finalUrl = redirectedUrl;
                    } else break;
                } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
                connection.disconnect();

                img = ImageIO.read(new URL(finalUrl));
            } catch (IOException e1) {
                log.error(e1);
                event.getChannel().sendMessage("Unable to download image").queue();
                return guildEntity;
            }

            // set jpeg compression
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.0001f);

            // strip alpha channel
            BufferedImage result =
                    new BufferedImage(
                            image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            result.createGraphics().drawImage(img, 0, 0, Color.BLACK, null);

            ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream imageOutputStream =
                    new MemoryCacheImageOutputStream(writerOutput);
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            writer.setOutput(imageOutputStream);
            try {
                writer.write(null, new IIOImage(result, null, null), jpegParams);
                event.getChannel()
                        .sendMessage("Here you go")
                        .addFile(writerOutput.toByteArray(), "morejpeg.jpg")
                        .queue();
            } catch (IOException e) {
                log.error(e);
            }
        }

        return guildEntity;
    }
}
