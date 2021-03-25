package neptune.commands.ImageCommands;

import neptune.commands.ICommand;
import neptune.commands.CommandHelpers;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.sourceforge.tess4j.Tesseract;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import javax.imageio.ImageIO;

public class ocr implements ICommand {
    protected static final Logger log = LogManager.getLogger();
    String tessdata = "dependencies" + File.separator + "tessdata";
    CommandHelpers helpers = new CommandHelpers();

    @Override
    public void run(
            GuildMessageReceivedEvent event, String messageContent) {
        Tesseract tesseract = new Tesseract();
        tesseract.setOcrEngineMode(1);
        try {
            String ImageUrl = helpers.getImageUrl(event);
            if (ImageUrl != null) {
                BufferedImage img = ImageIO.read(new URL(ImageUrl));
                BufferedImage greyImage =
                        new BufferedImage(
                                img.getWidth(), img.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
                Graphics g = greyImage.getGraphics();
                g.drawImage(img, 0, 0, null);
                g.dispose();

                tesseract.setDatapath(tessdata);
                String text = tesseract.doOCR(greyImage);
                event.getChannel().sendMessage(text).queue();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
