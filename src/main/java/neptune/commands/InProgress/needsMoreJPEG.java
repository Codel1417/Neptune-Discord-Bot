package neptune.commands.InProgress;

import neptune.commands.CommandInterface;
import neptune.commands.commandCategories;
import neptune.storage.VariablesStorage;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Iterator;

public class needsMoreJPEG implements CommandInterface {
    @Override
    public String getName() {
        return "Needs More JEPG";
    }

    @Override
    public String getCommand() {
        return "jpeg";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Fun;
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
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        File input = null;
        byte[] data = new byte[0];
        //List<Message.Attachment> imageAttachments;
        //imageAttachments = event.getChannel().getHistory().retrievePast(2).complete().get(1).getAttachments();
        String fileURL = event.getChannel().getHistory().retrievePast(2).complete().get(1).getEmbeds().get(0).getImage().getUrl();
        try {
            try (BufferedInputStream inputStream = new BufferedInputStream(new URL(fileURL).openStream());
                 FileOutputStream fileOS = new FileOutputStream("")) {
                data = new byte[1024];
                int byteContent;
                while ((byteContent = inputStream.read(data, 0, 1024)) != -1) {
                    //fileOS.write(data, 0, byteContent);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (input == null){
            return false;
        }

        try {
            //File input = new File("original_image.jpg");
            InputStream targetStream = new ByteArrayInputStream(data);
            BufferedImage image = ImageIO.read(targetStream);

            File compressedImageFile = new File("compressed_image.jpg");
            OutputStream os = new FileOutputStream(compressedImageFile);

            Iterator<ImageWriter> writers = ImageIO.getImageWritersByFormatName("jpg");
            ImageWriter writer = (ImageWriter) writers.next();

            ImageOutputStream ios = ImageIO.createImageOutputStream(os);
            writer.setOutput(ios);

            ImageWriteParam param = writer.getDefaultWriteParam();

            param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
            param.setCompressionQuality(0.5f);  // Change the quality value you prefer
            writer.write(null, new IIOImage(image, null, null), param);
            event.getChannel().sendFile(compressedImageFile).queue();
            os.close();
            ios.close();
            writer.dispose();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}
