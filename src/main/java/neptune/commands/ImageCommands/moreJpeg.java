package neptune.commands.ImageCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;
import javax.imageio.stream.MemoryCacheImageOutputStream;

public class moreJpeg implements ICommand {
    protected static final Logger log = LogManager.getLogger();
    final Helpers helpers = new Helpers();

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        try {
            String finalUrl = helpers.getImageUrl(event);
            if (finalUrl != null) {
                BufferedImage img = ImageIO.read(new URL(finalUrl));

                // set jpeg compression
                JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
                jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                jpegParams.setCompressionQuality(0.0001f);
                // strip alpha channel
                BufferedImage result =
                        new BufferedImage(
                                img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
                result.createGraphics().drawImage(img, 0, 0, Color.BLACK, null);

                ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
                MemoryCacheImageOutputStream imageOutputStream =
                        new MemoryCacheImageOutputStream(writerOutput);
                final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
                writer.setOutput(imageOutputStream);
                writer.write(null, new IIOImage(result, null, null), jpegParams);
                EmbedBuilder embedBuilder = new EmbedBuilder();
                event.getChannel()
                        .sendMessage("Here you go")
                        .addFile(writerOutput.toByteArray(), "morejpeg.jpg")
                        .queue();
                //TODO: Blocking. Need to either, Upload to CDN to use Message object, or Host on server. Discord will cache the url so my server is never directly hit.
                return null;
            }
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }
        return builder.setContent("Unable to jpeg-ify your image, Was there an image to begin with?").build();
    }
}
