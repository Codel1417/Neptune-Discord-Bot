package neptune.storage;

import com.google.gson.internal.LinkedTreeMap;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

import java.net.URL;
import java.util.ArrayList;
import java.util.Random;

public class TenorConnection extends ConvertJSON{

    private String API_KEY;
    public TenorConnection(String API_KEY){
        this.API_KEY = API_KEY;
    }

    public String getSingleImage(String SearchTerm){
        String returnURL;
        String resultLimit = "15";
        SearchTerm = SearchTerm.replaceAll(" ","-");
        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=%3$s",
                SearchTerm, API_KEY, resultLimit);
        System.out.println("Link " + url);
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            System.out.println("Response Code = " + connection.getResponseCode());
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
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
            LinkedTreeMap<String,Object> imageEntry = imageArray.get(random.nextInt(imageArray.size()));

            ArrayList<LinkedTreeMap> mediaList = (ArrayList<LinkedTreeMap>) imageEntry.get("media");
            LinkedTreeMap media =  mediaList.get(0);
            LinkedTreeMap gif = (LinkedTreeMap) media.get("gif");
            return (String) gif.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnURL = "";
        return returnURL;
    }
}
