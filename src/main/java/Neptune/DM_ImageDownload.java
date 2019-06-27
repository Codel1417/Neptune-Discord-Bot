package Neptune;

import net.dv8tion.jda.core.entities.Message;
import net.dv8tion.jda.core.entities.MessageEmbed;
import net.dv8tion.jda.core.events.message.priv.PrivateMessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.io.IOException;

public class DM_ImageDownload extends ListenerAdapter {
    private FileDownloader fileDownloader = new FileDownloader();
    @Override
    public void onPrivateMessageReceived(PrivateMessageReceivedEvent event) {
        //checks if the message was sent from a bot
        if (event.getMessage().getAttachments() != null){
            for (Message.Attachment attachment : event.getMessage().getAttachments()){
                File attach = new File("DM_Files" + File.separator + attachment.getFileName());
                if(!attach.exists()){
                    attachment.download(new File("DM_Files" + File.separator + attachment.getFileName()));
                }
            }
        }
        if (event.getMessage().getEmbeds() != null) {
            for (MessageEmbed messageEmbed : event.getMessage().getEmbeds()) {
                messageEmbed.getImage().getUrl();
                if (messageEmbed.getImage().getUrl() != null){
                    try {
                        fileDownloader.DownloadFile(messageEmbed.getImage().getUrl(), "DM_Files" + File.separator + System.currentTimeMillis() + ".png", "image");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
