package neptune.commands.ImageCommands;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.MemoryCacheImageOutputStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.awt.Graphics2D;

import java.awt.AlphaComposite;
import java.util.Objects;

import neptune.commands.ICommand;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class bonkImage implements ICommand {
    protected static final Logger log = LogManager.getLogger();
    final URL backgroundImageURL = getClass().getResource("/src/main/resources/images/bonk.png");
    final URL batImageUrl = getClass().getResource("/src/main/resources/images/bat.png");
    @Override
    public void run(GuildMessageReceivedEvent event, String messageContent) {
        try {
            BufferedImage avatarIcon = ImageIO.read(new URL(Objects.requireNonNull(event.getAuthor().getAvatarUrl())));
            
            //this will be imported from the jar
            BufferedImage overlay = ImageIO.read(Objects.requireNonNull(batImageUrl));
            BufferedImage finalimage = ImageIO.read(Objects.requireNonNull(backgroundImageURL));
            Graphics2D g2d = finalimage.createGraphics();
            g2d.setComposite(AlphaComposite.SrcOver.derive(1f));
            //TODO: Set image cordinates
            g2d.drawImage(avatarIcon, 0, 0, null);
            g2d.drawImage(overlay, 0, 0, null);

            g2d.dispose();
            
            ByteArrayOutputStream writerOutput = new ByteArrayOutputStream();
            MemoryCacheImageOutputStream imageOutputStream =
                    new MemoryCacheImageOutputStream(writerOutput);
            final ImageWriter writer = ImageIO.getImageWritersByFormatName("png").next();
            writer.setOutput(imageOutputStream);
            writer.write(null, new IIOImage(finalimage, null, null), null);
            event.getChannel().sendMessage("Go to Horny Jail").addFile(writerOutput.toByteArray(), "bonk.png").queue();
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }
    }
    
}
