package neptune.commands.ImageCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
import net.dv8tion.jda.api.MessageBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.sourceforge.tess4j.Tesseract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

public class ocr implements ICommand {
    protected static final Logger log = LogManager.getLogger();
    private final Helpers helpers = new Helpers();
    private final Tesseract tesseract = new Tesseract();

    public ocr(){
        tesseract.setDatapath("tessdata");
    }

    @Override
    public Message run(GuildMessageReceivedEvent event, String messageContent, MessageBuilder builder) {
        try {
            String ImageUrl = helpers.getImageUrl(event);
            if (ImageUrl != null) {
                BufferedImage img = ImageIO.read(new URL(ImageUrl));
                BufferedImage greyImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = greyImage.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();
                // if this file does not exist java will crash
                String text = tesseract.doOCR(greyImage);
                return builder.setContent(text).build();
            }
        } catch (Exception e) {
            log.error(e);
            Sentry.captureException(e);
        }
        return builder.setContent("Unable to find image to search for text").build();
    }
}
