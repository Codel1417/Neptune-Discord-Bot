package Neptune.Commands.FunCommands;

import Neptune.Commands.CommandInterface;
import Neptune.Commands.CommonMethods;
import Neptune.Commands.commandCategories;
import Neptune.Storage.VariablesStorage;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;
import net.dv8tion.jda.core.EmbedBuilder;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;

import javax.net.ssl.HttpsURLConnection;
import java.awt.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

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
        return commandCategories.Fun;
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
    public boolean getRequireOwner() {
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
    public boolean run(MessageReceivedEvent event, VariablesStorage variablesStorage, String messageContent) {
        String search = getCommandName(messageContent)[0]; //get first entry for now
        //TODO: handle whitespace

        //errors are handled by breaking the rest of the command

        //get content from imgur
        try {
            URL url = new URL("https://api.imgur.com/3/gallery/search/viral/all/1?q=" + search);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url.openConnection();
            httpsURLConnection.setRequestMethod("GET");
            httpsURLConnection.setRequestProperty("Content-Type", "application/json");
            httpsURLConnection.setRequestProperty("Authorization"," Client-ID b1a9974a8f499db");
            System.out.println("Response Code = " + httpsURLConnection.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(httpsURLConnection.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            httpsURLConnection.disconnect();

            //Convert json into readable format
            Gson gson = new GsonBuilder().create();
            Type typeOfHashMap = new TypeToken<LinkedTreeMap>() { }.getType();
            LinkedTreeMap json = gson.fromJson(content.toString(), typeOfHashMap);

            //move through data
            ArrayList<LinkedTreeMap> results = (ArrayList<LinkedTreeMap>) json.get("data");

            //remove unwanted entries
            ArrayList<LinkedTreeMap> temp = (ArrayList<LinkedTreeMap>) results.clone(); //clone list to skip error.
            ArrayList<LinkedTreeMap> ImageList = new ArrayList<>();


            for(LinkedTreeMap map : temp){
                if(map.get("nsfw").toString().equalsIgnoreCase("true")){
                    results.remove(map);
                }
                else{
                    if(map.get("is_album").toString().equalsIgnoreCase("true")){
                        ArrayList<LinkedTreeMap> list = (ArrayList<LinkedTreeMap>) map.get("images");
                        ImageList.addAll(list);
                    }
                    else{
                        ImageList.add(map);
                    }
                }
            }

            if (ImageList.size() == 0){
                EmbedBuilder embedBuilder = new EmbedBuilder();
                embedBuilder.setTitle("Imgur");
                embedBuilder.setColor(Color.RED);
                embedBuilder.setDescription("Unable to find any Images :(");
                event.getChannel().sendMessage(embedBuilder.build()).queue();
                return false;

            }
            LinkedTreeMap image = ImageList.get(random.nextInt(ImageList.size())); //the image details
            String ImageURL = (String) image.get("link");

            //prepare message
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setImage(ImageURL)
                    .setColor(Color.MAGENTA)
                    .setDescription("I Found this for you")
                    .setTitle("Imgur");
            event.getChannel().sendMessage(embedBuilder.build()).queue();
            return true;
        } catch (Exception e){
            e.printStackTrace();
        }
        return false;
    }
}
