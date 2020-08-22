package neptune.commands;

import neptune.storage.commandLineOptionsSingleton;
import neptune.webConnection.TenorConnection;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.util.List;

public abstract class TenorGif {
    //GetAuthToken getAuthToken = new GetAuthToken();
    private final TenorConnection tenorConnection = new TenorConnection(commandLineOptionsSingleton.getInstance().getOptions().getOptionValue("t"));

    protected EmbedBuilder getImageDefaultEmbed(GuildMessageReceivedEvent event, String Search, boolean MentionMessage) {
        List<IMentionable> Mentions = event.getMessage().getMentions();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder mentionList = new StringBuilder();
        for (IMentionable mentionable : Mentions) {
            mentionList.append(mentionable.getAsMention()).append(" ");
        }
        if (mentionList.toString().equalsIgnoreCase("") || !MentionMessage) {
            embedBuilder.setDescription("");
        } else {
            embedBuilder.setDescription("Neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(tenorConnection.getSingleImage("Anime Girl " +  Search));

        return embedBuilder;
    }
    protected EmbedBuilder getImageEmbed(GuildMessageReceivedEvent event, String Search, boolean MentionMessage, String CustomMessage) {
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
            embedBuilder.setDescription("Neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(tenorConnection.getSingleImage("Anime Girl " +  Search));

        return embedBuilder;
    }
}

