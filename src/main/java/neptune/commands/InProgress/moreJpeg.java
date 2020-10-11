package neptune.commands.InProgress;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.ImageOutputStream;
import javax.imageio.stream.ImageOutputStreamImpl;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class moreJpeg implements CommandInterface {

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
        return commandCategories.Dev;
    }

    @Override
    public String getHelp() {
        return null;
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
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty()){
            Attachment image = attachments.get(0);    
            BufferedImage img;
            HttpURLConnection connection;
            String finalUrl = image.getUrl();
            try {
                do {
                    connection = (HttpURLConnection) new URL(finalUrl)
                            .openConnection();
                    connection.setInstanceFollowRedirects(false);
                    connection.setUseCaches(false);
                    connection.setRequestMethod("GET");
                    connection.connect();
                    int responseCode = connection.getResponseCode();
                    if (responseCode >= 300 && responseCode < 400) {
                        String redirectedUrl = connection.getHeaderField("Location");
                        if (null == redirectedUrl)
                            break;
                        finalUrl = redirectedUrl;
                        System.out.println("redirected url: " + finalUrl);
                    } else
                        break;
                } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
                connection.disconnect();


                img = ImageIO.read(new URL(finalUrl));
            } catch (IOException e1) {
                e1.printStackTrace();
                event.getChannel().sendMessage("Unable to download image").queue();
                return guildEntity;
            }
    
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.1f);
            ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(writerOutput);
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            // specifies where the jpg image has to be written
            writer.setOutput(imageOutputStream);
            // writes the file with given compression level
            // from your JPEGImageWriteParam instance
            try {
                writer.write(null, new IIOImage(img, null, null), jpegParams);
                //ImageIO.write(img, "jpg", writerOutput);
                event.getChannel().sendMessage("Heree you go").addFile(writerOutput.toByteArray(), "morejpeg.jpg").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return guildEntity;
    }
    
}
