package Neptune.Storage;

import com.google.gson.internal.LinkedTreeMap;

import java.io.IOException;
import java.net.HttpURLConnection;

import java.net.URL;

public class TenorConnection extends ConvertJSON{

    private String API_KEY;
    public TenorConnection(String API_KEY){
        this.API_KEY = API_KEY;
    }

    public String getingleImage(String SearchTerm){
        String returnURL;
        String resultLimit = "25";
        final String url = String.format("https://api.tenor.com/v1/search?q=%1$s&key=%2$s&limit=%3$s",
                SearchTerm, API_KEY, resultLimit);
        HttpURLConnection connection = null;
        try {
            // Get request
            connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("GET");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            String response = connection.getResponseMessage();

            LinkedTreeMap JSON = fromJSON(response);
        } catch (IOException e) {
            e.printStackTrace();
        }
        returnURL = "";
        return returnURL;
    }
}
