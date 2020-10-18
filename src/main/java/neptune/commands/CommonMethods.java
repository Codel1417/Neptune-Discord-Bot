package neptune.commands;

import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.Message.Attachment;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.imageio.ImageIO;

public class CommonMethods {
    protected static final Logger log = LogManager.getLogger();

    protected String[] getCommandName(String MessageContent) {
        String[] splitStr = MessageContent.trim().split("\\s+");
        String[] returnText = new String[2];
        if (splitStr.length == 1) {
            returnText[0] = splitStr[0].trim();
            returnText[1] = "";
        } else {
            returnText[0] = splitStr[0];
            returnText[1] = MessageContent.trim().substring(splitStr[0].length()).trim();
        }
        return returnText;
    }

    protected String getEnabledDisabledIcon(Boolean value) {
        String enabled = "\u2705";
        String disabled = "\u274C";

        if (value) {
            return enabled;
        } else return disabled;
    }

    protected String getEnabledDisabledIconText(Boolean value) {
        String enabled = "\u2705 Enabled";
        String disabled = "\u274C Disabled";

        if (value) {
            return enabled;
        } else return disabled;
    }

    public String getImageUrl(GuildMessageReceivedEvent event) throws IOException {
        // attachment pass
        Attachment current;
        List<Attachment> attachments = event.getMessage().getAttachments();
        if (!attachments.isEmpty()) {
            current = attachments.get(0);
            if (isImage(current.getFileExtension())) {
                return getFinalURl(current.getProxyUrl());
            }
        }
        List<MessageEmbed> embeds = event.getMessage().getEmbeds();
        if (!embeds.isEmpty()) {
            if (embeds.get(0).getImage() != null) {
                return getFinalURl(embeds.get(0).getImage().getProxyUrl());
            } else if (embeds.get(0).getThumbnail() != null) {
                return getFinalURl(embeds.get(0).getThumbnail().getProxyUrl());
            }
        }
        // if current message has no media try previous message
        List<Message> messages = event.getChannel().getHistory().retrievePast(2).complete();
        if (messages.size() == 2) {
            Message message = messages.get(1);
            // attachment pass
            attachments = message.getAttachments();
            if (!attachments.isEmpty()) {
                current = attachments.get(0);
                if (isImage(current.getFileExtension())) {
                    return getFinalURl(current.getProxyUrl());
                }
            }
            embeds = message.getEmbeds();
            if (!embeds.isEmpty()) {
                if (embeds.get(0).getImage() != null) {
                    return getFinalURl(embeds.get(0).getImage().getProxyUrl());
                } else if (embeds.get(0).getThumbnail() != null) {
                    return getFinalURl(embeds.get(0).getThumbnail().getProxyUrl());
                }
            }
        }
        return null;
    }

    String getFinalURl(String url) throws IOException {
        log.trace("Try Url: " + url);
        String FinalURl = url;
        HttpURLConnection connection;
        do {
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setInstanceFollowRedirects(false);
            connection.setUseCaches(false);
            connection.setRequestMethod("GET");
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode >= 300 && responseCode < 400) {
                String redirectedUrl = connection.getHeaderField("Location");
                if (null == redirectedUrl) break;
                FinalURl = redirectedUrl;
            } else break;
        } while (connection.getResponseCode() != HttpURLConnection.HTTP_OK);
        connection.disconnect();
        log.trace("Final Url: " + FinalURl);
        return FinalURl;
    }
    public boolean isImage(String ext){
        String names[] = ImageIO.getReaderFormatNames();
        for(String name: names){
            if (name.equalsIgnoreCase(ext)){
                return true;
            }
        }
        return false;
    }
}
