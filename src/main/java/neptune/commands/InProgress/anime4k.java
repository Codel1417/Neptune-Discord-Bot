package neptune.commands.InProgress;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.imageio.stream.MemoryCacheImageOutputStream;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import com.bloc97.anime4k.ImageKernel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class anime4k implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();

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
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty()){
            Attachment image = attachments.get(0);    
            BufferedImage img;

            //This attempts to get the final image url after redirects
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
                    } else
                        break;
                } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
                connection.disconnect();


                img = ImageIO.read(new URL(finalUrl));
                ImageKernel kernel = new ImageKernel();

                float scale = 2f;
                float pushStrength = scale / 6f;
                float pushGradStrength = scale / 2f;

                //upscale image
                img = scale(img, (int)(img.getWidth() * scale), (int)(img.getHeight() * scale));


                //enhance image
                kernel.setPushStrength(pushStrength);
                kernel.setPushGradStrength(pushGradStrength);
                kernel.setBufferedImage(img);
                kernel.process();
                kernel.updateBufferedImage();


                //upload to discord
                ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
                MemoryCacheImageOutputStream imageOutputStream = new MemoryCacheImageOutputStream(writerOutput);
                byte[] byteOutput;
                ImageIO.write(img, "png", imageOutputStream);
                byteOutput = writerOutput.toByteArray();

                //downscale image until i can upload it
                while (byteOutput.length > 8388608)  {
                    byteOutput = null;
                    writerOutput = new ByteArrayOutputStream();
                    imageOutputStream = new MemoryCacheImageOutputStream(writerOutput);
                    log.warn("Downscaling image");
                    img = scale(img, (int)(img.getWidth() * 0.95), (int)(img.getHeight() * 0.95));
                    ImageIO.write(img, "png", imageOutputStream);
                    byteOutput = writerOutput.toByteArray();
                    log.warn(byteOutput.length);
                } 

                event.getChannel().sendMessage("Here you go").addFile(byteOutput,"upscaled.png").queue();


            } catch (IOException e1) {
                log.error(e1);
                event.getChannel().sendMessage("Unable to download image").queue();
                return guildEntity;
            }
        }
        else {event.getChannel().sendMessage("Please attach an immage with this command.").queue();}
        return guildEntity;
    }
    static BufferedImage copyType(BufferedImage bi) {
        BufferedImage newImage = new BufferedImage(bi.getWidth(), bi.getHeight(), BufferedImage.TYPE_INT_ARGB);
        newImage.getGraphics().drawImage(bi, 0, 0, null);
        return newImage;
    }
    static BufferedImage scale(BufferedImage bi, int width, int height) {
        BufferedImage newImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = (Graphics2D)newImage.getGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.drawImage(bi, 0, 0, width, height, null);
        return newImage;
    }
}
