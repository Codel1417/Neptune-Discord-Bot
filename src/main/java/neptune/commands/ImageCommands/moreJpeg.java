package neptune.commands.ImageCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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

public class moreJpeg implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();
    CommonMethods helpers = new CommonMethods();

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
                event.getChannel()
                        .sendMessage("Here you go")
                        .addFile(writerOutput.toByteArray(), "morejpeg.jpg")
                        .queue();
            }
        } catch (IOException e) {
            log.error(e);
        }
        return guildEntity;
    }
}
