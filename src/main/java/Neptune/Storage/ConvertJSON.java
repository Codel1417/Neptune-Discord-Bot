package Neptune.Storage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class ConvertJSON {
    public String toJSON(LinkedTreeMap<String, Object> data){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);
    }
    public String toJSON(ArrayList<LinkedTreeMap<String,String>> data){
        Gson gson = new GsonBuilder().create();
        return gson.toJson(data);
    }
    public LinkedTreeMap<String, Object> fromJSON(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type typeOfHashMap = new TypeToken<LinkedTreeMap<String, Object>>() { }.getType();
        return gson.fromJson(json, typeOfHashMap);
    }
    public ArrayList<LinkedTreeMap<String, String>> fromJSONList(String json){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type typeOfLinkedList = new TypeToken<ArrayList<LinkedTreeMap<String, String>>>() { }.getType();
        return gson.fromJson(json, typeOfLinkedList);
    }
}
