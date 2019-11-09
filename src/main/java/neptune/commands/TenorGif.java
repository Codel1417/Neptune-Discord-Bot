package neptune.commands;

import neptune.storage.GetAuthToken;
import neptune.storage.TenorConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class TenorGif {
    GetAuthToken getAuthToken = new GetAuthToken();
    Map authKeys = getAuthToken.GetToken(new File("NepAuth.json"));
    private final TenorConnection tenorConnection = new TenorConnection((String) authKeys.get("tenor"));

    protected EmbedBuilder getImageDefaultEmbed(MessageReceivedEvent event, String Search, boolean MentionMessage) {
        List<IMentionable> Mentions = event.getMessage().getMentions();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder mentionList = new StringBuilder();
        for (IMentionable mentionable : Mentions) {
            mentionList.append(mentionable.getAsMention()).append(" ");
        }
        if (mentionList.toString().equalsIgnoreCase("") || !MentionMessage) {
            embedBuilder.setDescription("");
        } else {
            embedBuilder.setDescription("neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(tenorConnection.getSingleImage("Anime Girl " +  Search));

        return embedBuilder;
    }
    protected EmbedBuilder getImageEmbed(MessageReceivedEvent event, String Search, boolean MentionMessage, String CustomMessage) {
        List<IMentionable> Mentions = event.getMessage().getMentions();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder mentionList = new StringBuilder();
        for (IMentionable mentionable : Mentions) {
            mentionList.append(mentionable.getAsMention()).append(" ");
        }
        if (mentionList.toString().equalsIgnoreCase("")) {
            embedBuilder.setDescription("");
        } else if (CustomMessage != null){
            embedBuilder.setDescription(CustomMessage + " " + mentionList);
        } else {
            embedBuilder.setDescription("neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(tenorConnection.getSingleImage("Anime Girl " +  Search));

        return embedBuilder;
    }
}

