package neptune.commands.ImageCommands;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.sourceforge.tess4j.Tesseract;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jgit.api.Git;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URL;
import java.nio.file.Paths;

import javax.imageio.ImageIO;

public class ocr implements CommandInterface {
    protected static final Logger log = LogManager.getLogger();
    String tessdata = "dependentcies" + File.separator + "tessdata";
    CommonMethods helpers = new CommonMethods();

    public ocr() {
        String repoUrl = "https://github.com/tesseract-ocr/tessdata_best.git";
        try {
            // FileUtils.deleteDirectory(new File(tessdata));
            Git.cloneRepository().setURI(repoUrl).setDirectory(Paths.get(tessdata).toFile()).call();
        } catch (Exception e) {
            log.error(e);
            // log.fatal("Unable to Download tessdata");
        }
    }

    @Override
    public String getName() {
        return "Optical Character Recognition";
    }

    @Override
    public String getCommand() {
        return "ocr";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Image;
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
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        Tesseract tesseract = new Tesseract();
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
        return guildEntity;
    }
}
