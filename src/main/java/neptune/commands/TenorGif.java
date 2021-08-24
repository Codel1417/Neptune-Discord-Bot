package neptune.commands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import io.sentry.Sentry;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class TenorGif {

    protected EmbedBuilder getImageDefaultEmbed(
            GuildMessageReceivedEvent event, String Search, boolean MentionMessage) {
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
        embedBuilder.setImage(getSingleImage("Anime Girl " + Search));

        return embedBuilder;
    }

    protected EmbedBuilder getImageEmbed(
            GuildMessageReceivedEvent event,
            String Search,
            boolean MentionMessage,
            String CustomMessage) {
        List<IMentionable> Mentions = event.getMessage().getMentions();
        EmbedBuilder embedBuilder = new EmbedBuilder();
        StringBuilder mentionList = new StringBuilder();
        for (IMentionable mentionable : Mentions) {
            mentionList.append(mentionable.getAsMention()).append(" ");
        }
        if (mentionList.toString().equalsIgnoreCase("")) {
            embedBuilder.setDescription("");
        } else if (CustomMessage != null) {
            embedBuilder.setDescription(CustomMessage + " " + mentionList);
        } else {
            embedBuilder.setDescription("Neptune " + Search + "s " + mentionList);
        }
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by Tenor.com", null);
        embedBuilder.setImage(getSingleImage("Anime Girl " + Search));

        return embedBuilder;
    }

    protected static final Logger log = LogManager.getLogger();
    private final String API_KEY = System.getenv("NEPTUNE_TENOR_TOKEN");
    
    public String getSingleImage(String SearchTerm) {
        String returnURL;
        SearchTerm = SearchTerm.replaceAll(" ", "-");
        final String url =
                String.format(
                        "https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=15&contentfilter=high&media_filter=minimal",
                        SearchTerm, API_KEY);
        log.debug("Link " + url);
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            log.debug("Response Code = " + connection.getResponseCode());
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();

            JsonNode jsonNode = new ObjectMapper().readTree(content.toString());
            TypeReference<ArrayList<JsonNode>> typeRefImageArray =
                    new TypeReference<>() {
                    };
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader ImageListReader = mapper.readerFor(typeRefImageArray);
            ArrayList<JsonNode> imageArray = ImageListReader.readValue(jsonNode.get("results"));
            Random random = new Random();
            JsonNode imageEntry = imageArray.get(random.nextInt(imageArray.size()));
            return imageEntry.get("media").get(0).get("gif").get("url").asText();
        } catch (IOException e) {
            log.error(e);
            Sentry.captureException(e);
        }
        returnURL = "";
        return returnURL;
    }
}
