package Neptune.Storage;

import com.google.gson.internal.LinkedTreeMap;
import net.dv8tion.jda.core.entities.Guild;

import java.util.HashMap;
import java.util.Map;


public class StorageController extends ConvertJSON {

    private static StorageController ourInstance = new StorageController();

    public static StorageController getInstance() {
        return ourInstance;
    }

    private StorageController() {
    }

    private SQLiteConnection databaseConnection = new SQLiteConnection();

    public boolean addGuild(Guild Guild) {
        //Need to add something to map beforehand;
        LinkedTreeMap<String, Object> temp = new LinkedTreeMap<>();
        temp.put("ID",Guild.getId());
        try {
            return databaseConnection.addData(Guild.getId(),toJSON(temp));
        } catch (MissingDataException e) {
            e.printStackTrace();
            return false;
        }
    }

    public Map getGuild(Guild Guild) {
        return fromJSON(databaseConnection.getData(Guild.getId()));
    }

    public boolean updateGuildField(Guild Guild, String Field, Object Object) {
        Map GuildInfo = getGuild(Guild);
        GuildInfo.put(Field,Object);
        try {
            databaseConnection.updateData(Guild.getId(),toJSON((LinkedTreeMap<String, java.lang.Object>) GuildInfo));
            return true;
        } catch (MissingDataException e) {
            return false;
        }
    }

    public boolean incrementAnalyticForCommand(String command) {
        LinkedTreeMap<String, Object> Analytics;
        boolean result;

        String AnalyticsRaw = databaseConnection.getData("Analytics");

        if (AnalyticsRaw == null){
            Analytics = new LinkedTreeMap<>();
        }
        else {
            Analytics = fromJSON(AnalyticsRaw);
        }
        int value = (int) Analytics.getOrDefault(command,0);
        value++;
        Analytics.put(command,value);
        String json = toJSON(Analytics);
        try {
            if (AnalyticsRaw == null) {
                result = databaseConnection.addData("Analytics",json);
            }
            else{
                result = databaseConnection.updateData("Analytics",json);
            }
            return result;
        } catch (MissingDataException e) {
            e.printStackTrace();
            return false;
        }
    }

    private HashMap<String, Object> commandList;

    public HashMap<String, Object> getCommandList() {
        return commandList;
    }

    public void setCommandList(HashMap<String, Object> commandList) {
        this.commandList = commandList;
    }
}
