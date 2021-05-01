package neptune.commands.ImageCommands;

import neptune.commands.ICommand;
import neptune.commands.Helpers;
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
    private String tessdata = File.separator + "tessdata";
    private Helpers helpers = new Helpers();
    private Tesseract tesseract = new Tesseract();

    public ocr(){
        tesseract.setDatapath(tessdata);
        File tessCheck = new File(tessdata);
        if (!tessCheck.exists()){
            log.fatal("Tessdata not found");
        }
    }
    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
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
                event.getChannel().sendMessage(text).queue();
            }
        } catch (Exception e) {
            log.error(e);
        }
    }
}
