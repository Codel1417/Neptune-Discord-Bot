package neptune.commands.InProgress;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class VRC extends CommonMethods implements CommandInterface {
    @Override
    public String getName() {
        return "VRChat";
    }

    @Override
    public String getCommand() {
        return "vrc";
    }

    @Override
    public String getDescription() {
        return "";
    }

    @Override
    public commandCategories getCategory() {
        return commandCategories.Utility;
    }

    @Override
    public String getHelp() {
        return "";
    }

    @Override
    public boolean getRequireManageServer() {
        return false;
    }

    @Override
    public boolean getHideCommand() {
        return true;
    }

    @Override
    public boolean getRequireManageUsers() {
        return false;
    }

    @Override
    public guildObject run(
            GuildMessageReceivedEvent event, String messageContent, guildObject guildEntity) {
        String[] command = getCommandName(messageContent);
        switch (command[0]) {
            case "world":
                {
                    getWorldByID(command[1], event);
                    break;
                }
            case "online":
                {
                    getOnline(event);
                    break;
                }
            default:
                displayMenu(event);
        }
        return guildEntity;
    }

    private void getOnline(GuildMessageReceivedEvent event) {
        // API
        String result = getOnlineUsers();
        // Send Message
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.addField("VRChat Users Currently Online", result + " users online", true);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private void getWorldByID(String search, GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = getEmbedBuilder();

        // API
        Map<String, String> result = null;
        try {
            result = getWorldByID(search);
            if (!result.getOrDefault("name", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Name", result.get("name"), true);
            }
            if (!result.getOrDefault("description", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Description", result.get("description"), true);
            }
            if (!result.getOrDefault("authorName", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Author", result.get("authorName"), true);
            }
            if (!result.getOrDefault("image", "").equalsIgnoreCase("")) {
                embedBuilder.setImage(result.get("image"));
            }
            if (!result.getOrDefault("capacity", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Instance Capacity", result.get("capacity") + " users", true);
            }
            if (!result.getOrDefault("visits", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Visits", result.get("visits") + " users", true);
            }
            if (!result.getOrDefault("occupants", "").equalsIgnoreCase("")) {
                embedBuilder.addField("Occupants", result.get("occupants") + " users", true);
            }
        } catch (NullPointerException e) {
            embedBuilder.setDescription("World ID does not exist or threw an error");
        }

        // Send Message
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    private EmbedBuilder getEmbedBuilder() {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("VRChat");
        embedBuilder.setColor(Color.MAGENTA);
        embedBuilder.setFooter("Powered by VRChat", "https://vrchat.com/public/media/logo.png");
        return embedBuilder;
    }

    private void displayMenu(GuildMessageReceivedEvent event) {
        EmbedBuilder embedBuilder = getEmbedBuilder();
        embedBuilder.setTitle("VRChat Help");
        embedBuilder.setDescription("Interact with the VRChat API");
        embedBuilder.addField("Get Users Online", "!nep " + getCommand() + " online", false);
        embedBuilder.addField("World ID Search", "!nep " + getCommand() + " world <ID>", false);
        embedBuilder.addField("User ID Search", "!nep " + getCommand() + " user <ID>", false);
        event.getChannel().sendMessage(embedBuilder.build()).queue();
    }

    public Map<String, String> getWorldByID(String worldID) {
        HashMap<String, String> result = new HashMap<>();
        JsonParser parser = new JsonParser();
        String content = httpRequest(urlFormatter("worlds/" + worldID), "GET");
        JsonObject jsonObject = parser.parse(content).getAsJsonObject();

        result.put("name", jsonObject.get("name").getAsString());
        result.put("image", jsonObject.get("imageUrl").getAsString());
        result.put("description", jsonObject.get("description").getAsString());
        result.put("visits", jsonObject.get("visits").getAsString());
        result.put("occupants", jsonObject.get("occupants").getAsString());
        result.put("authorName", jsonObject.get("authorName").getAsString());
        result.put("assetUrl", jsonObject.get("assetUrl").getAsString());
        // populate map with values i care about;
        return result;
    }

    public Map<String, String> worldSearch(String search) {
        HashMap<String, String> result = new HashMap<>();
        JsonParser parser = new JsonParser();
        String content = "";
        // JsonElement jsonElement =  parser.parse(content.toString()).getAsJsonObject();
        // jsonElement.getAsJsonObject();
        return result;
    }

    public Map<String, String> userSearch(String user) {
        HashMap<String, String> result = new HashMap<>();
        String content = "";
        JsonParser parser = new JsonParser();
        // JsonElement jsonElement =  parser.parse(content.toString()).getAsJsonObject();
        // jsonElement.getAsJsonObject();
        return result;
    }

    public Map<String, String> getUserByID(String userID) {
        HashMap<String, String> result = new HashMap<>();
        return result;
    }

    public String getOnlineUsers() {
        String result = "";
        result = httpRequest(urlFormatter("visits"), "GET");
        return result;
    }

    private String apikey = "";
    protected static final Logger log = LogManager.getLogger();

    public String httpRequest(String URL, String httpMethod) {
        if (apikey.equalsIgnoreCase("")) {
            updateAuthToken();
        }
        log.debug("VRCHAT: Sending http " + httpMethod + " request: " + URL);
        HttpURLConnection connection = null;
        try {
            URL url = new URL(URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.toUpperCase());
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("user-agent", "VRChatJava");
            connection.setRequestProperty("Authorization", "Basic " + apikey);
            connection.setRequestProperty("Cookie", "auth=" + apikey);
            log.debug("VRCHAT: Response Code = " + connection.getResponseCode());

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        } catch (IOException e) {
            if (connection != null) {
                connection.disconnect();
            }
            e.printStackTrace();
        }
        return null;
    }

    public String urlFormatter(String request) {
        String apiurl = "https://api.vrchat.cloud/api/1";

        StringBuilder result = new StringBuilder();
        result.append(apiurl)
                .append("/")
                .append(request)
                .append("/")
                .append("?apiKey=")
                .append(apikey);

        return result.toString();
    }

    private void updateAuthToken() {
        String result = httpRequest(urlFormatter("config"), "Get");
        JsonParser parser = new JsonParser();
        JsonObject jsonObject = parser.parse(result).getAsJsonObject();
        apikey = jsonObject.get("apiKey").getAsString();
    }
}
