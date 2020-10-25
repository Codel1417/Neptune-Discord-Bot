package neptune.commands.ImageCommands;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;

import neptune.commands.CommandInterface;
import neptune.commands.CommonMethods;
import neptune.commands.commandCategories;
import neptune.storage.Guild.guildObject;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Imgur extends CommonMethods implements CommandInterface {
    Random random = new Random();

    @Override
    public String getName() {
        return "Imgur Search";
    }

    @Override
    public String getCommand() {
        return "imgur";
    }

    @Override
    public String getDescription() {
        return "Find images on Imgur";
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
        String search = getCommandName(messageContent)[0]; // get first entry for now

        // get content from imgur
        try {
            URL url = new URL("https://api.imgur.com/3/gallery/search/viral/all/1?q=" + search);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.setRequestProperty("Authorization", " Client-ID b1a9974a8f499db");

            BufferedReader in =
                    new BufferedReader(new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            httpsURLConnection.disconnect();

            JsonNode jsonNode = new ObjectMapper().readTree(content.toString());
            jsonNode = jsonNode.get("data");

            TypeReference<ArrayList<JsonNode>> typeRefImageArray =
                    new TypeReference<ArrayList<JsonNode>>() {};
            ObjectMapper mapper = new ObjectMapper();
            ObjectReader ImageListReader = mapper.readerFor(typeRefImageArray);

            ArrayList<JsonNode> ImageSearchList = ImageListReader.readValue(jsonNode);
            ArrayList<JsonNode> Images = new ArrayList<>();

            for (JsonNode jsonNode2 : ImageSearchList) {
                if (!jsonNode2.get("nsfw").asBoolean()) {
                    Images.add(jsonNode2.get("images"));
                }
            }

            if (Images.size() == 0) {
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Imgur");
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("Unable to find any Images :(");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return guildEntity;
            }
            JsonNode image = Images.get(random.nextInt(Images.size())).get(0); // the image details
            String ImageURL = image.get("link").asText();

            // prepare message
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder
                    .setImage(ImageURL)
                    .setColor(Color.MAGENTA)
                    .setTitle("Imgur")
                    .setDescription("I Found this for you")
                    .setFooter("Powered By Imgur", null);
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return guildEntity;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return guildEntity;
    }
}
