package Neptune.Commands;

import Neptune.Storage.VRCApiConnection;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.HashMap;
import java.util.Map;

public class VRChatRequest {
    VRCApiConnection vrcApiConnection= new VRCApiConnection();
    public Map<String, String> getWorldByID(String worldID){
        HashMap<String,String> result = new HashMap<>();
        JsonParser parser = new JsonParser();
        String content = vrcApiConnection.httpRequest(vrcApiConnection.urlFormatter("worlds/" + worldID),"GET");
        JsonObject jsonObject =  parser.parse(content).getAsJsonObject();

        result.put("name",jsonObject.get("name").getAsString());
        result.put("image",jsonObject.get("imageUrl").getAsString());
        result.put("description",jsonObject.get("description").getAsString());
        result.put("visits",jsonObject.get("visits").getAsString());
        result.put("occupants",jsonObject.get("occupants").getAsString());
        result.put("authorName",jsonObject.get("authorName").getAsString());

        //populate map with values i care about;

        return result;
    }
    public Map<String, String> worldSearch(String search){
        HashMap<String,String> result = new HashMap<>();
        JsonParser parser = new JsonParser();
        String content = "";
        //JsonElement jsonElement =  parser.parse(content.toString()).getAsJsonObject();
        //jsonElement.getAsJsonObject();
        return result;

    }
    public Map<String,String> userSearch(String user){
        HashMap<String,String> result = new HashMap<>();
        String content = "";
        JsonParser parser = new JsonParser();
        //JsonElement jsonElement =  parser.parse(content.toString()).getAsJsonObject();
        //jsonElement.getAsJsonObject();
        return result;

    }
    public Map<String,String> getUserByID(String userID){
        HashMap<String,String> result = new HashMap<>();
        return result;
    }
    public String getOnlineUsers(){
    String result = "";
    result = vrcApiConnection.httpRequest(vrcApiConnection.urlFormatter("visits"),"GET");
    return result;
    }
}
