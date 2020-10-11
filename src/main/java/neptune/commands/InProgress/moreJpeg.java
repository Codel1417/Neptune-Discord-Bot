package neptune.commands.InProgress;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.plugins.jpeg.JPEGImageWriteParam;


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
            URL url = this.getClass().getResource(image.getUrl());
            InputStream in;
            BufferedImage img;
            try {
                in = url.openStream();
                img = ImageIO.read(in);
            } catch (IOException e1) {
                e1.printStackTrace();
                event.getChannel().sendMessage("Unable to download image").queue();
                return guildEntity;
            }
    
            JPEGImageWriteParam jpegParams = new JPEGImageWriteParam(null);
            jpegParams.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            jpegParams.setCompressionQuality(0.1f);
    
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("jpg").next();
            ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
            // specifies where the jpg image has to be written
            writer.setOutput(writerOutput);
    
            // writes the file with given compression level
            // from your JPEGImageWriteParam instance
            try {
                writer.write(null, new IIOImage(img, null, null), jpegParams);
                event.getChannel().sendMessage("Here you go").addFile(writerOutput.toByteArray(), "morejpeg.jpg").queue();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return guildEntity;
    }
    
}
