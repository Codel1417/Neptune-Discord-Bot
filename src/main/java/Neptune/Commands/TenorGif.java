package Neptune.Commands;

import Neptune.Storage.GetAuthToken;
import Neptune.Storage.TenorConnection;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.entities.IMentionable;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import java.awt.*;
import java.io.File;
import java.util.List;
import java.util.Map;

public abstract class TenorGif {
    GetAuthToken getAuthToken = new GetAuthToken();
    Map authKeys = getAuthToken.GetToken(new File("NepAuth.json"));
    private final TenorConnection tenorConnection = new TenorConnection((String) authKeys.get("tenor"));

    protected EmbedBuilder getImageEmbed(MessageReceivedEvent event, String Search) {
        List<IMentionable> Mentions = event.getMessage().getMentions();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder mentionList = new StringBuilder();
        for (IMentionable mentionable : Mentions) {
            mentionList.append(mentionable.getAsMention()).append(" ");
        }
        if (mentionList.toString().equalsIgnoreCase("")) {
            embedBuilder.setDescription("");
        } else {
            embedBuilder.setDescription("Neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(tenorConnection.getingleImage("Anime Girl " +  Search));

        return embedBuilder;
    }
}

