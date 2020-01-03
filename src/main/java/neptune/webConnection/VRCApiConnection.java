package neptune.webConnection;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class VRCApiConnection {
    private String apikey = "";

    public String httpRequest(String URL, String httpMethod){
        if (apikey.equalsIgnoreCase("")){
            updateAuthToken();
        }
        System.out.println("VRCHAT: Sending http " + httpMethod + " request: " + URL);
        HttpURLConnection connection = null;
        try {
            java.net.URL url = new URL(URL);
            connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.toUpperCase());
            connection.setRequestProperty("X-Requested-With", "XMLHttpRequest");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("user-agent", "VRChatJava");
            connection.setRequestProperty("Authorization","Basic " +  apikey);
            connection.setRequestProperty("Cookie", "auth=" + apikey);
            System.out.println("VRCHAT: Response Code = " + connection.getResponseCode());

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuilder content = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            in.close();
            connection.disconnect();
            return content.toString();
        }
        catch (IOException e){
            if (connection != null) {
                connection.disconnect();
            }
            e.printStackTrace();
        }
        return null;
    }
    public String urlFormatter(String request){
        String apiurl = "https://api.vrchat.cloud/api/1";

        StringBuilder result = new StringBuilder();
        result.append(apiurl).append("/").append(request).append("/").append("?apiKey=").append(apikey);

        return result.toString();
    }
    private void updateAuthToken(){
        String result = httpRequest(urlFormatter("config"),"Get");
        JsonParser parser = new JsonParser();
        JsonObject jsonObject =  parser.parse(result).getAsJsonObject();
        apikey = jsonObject.get("apiKey").getAsString();

    }
}
