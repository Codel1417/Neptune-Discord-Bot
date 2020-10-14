package neptune.commands;

import neptune.storage.ConvertJSON;
import neptune.storage.commandLineOptionsSingleton;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.IMentionable;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.gson.internal.LinkedTreeMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TenorGif extends ConvertJSON {
  // GetAuthToken getAuthToken = new GetAuthToken();

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
  private String API_KEY =
      commandLineOptionsSingleton.getInstance().getOptions().getOptionValue("t");

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
      BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String inputLine;
      StringBuffer content = new StringBuffer();
      while ((inputLine = in.readLine()) != null) {
        content.append(inputLine);
      }
      in.close();
      connection.disconnect();

      LinkedTreeMap JSON = fromJSON(content.toString());
      ArrayList<LinkedTreeMap> imageArray = (ArrayList<LinkedTreeMap>) JSON.get("results");
      Random random = new Random();
      LinkedTreeMap<String, Object> imageEntry = imageArray.get(random.nextInt(imageArray.size()));

      ArrayList<LinkedTreeMap> mediaList = (ArrayList<LinkedTreeMap>) imageEntry.get("media");
      LinkedTreeMap media = mediaList.get(0);
      LinkedTreeMap gif = (LinkedTreeMap) media.get("gif");
      return (String) gif.get("url");
    } catch (IOException e) {
      log.error(e.toString());
    }
    returnURL = "";
    return returnURL;
  }
}
