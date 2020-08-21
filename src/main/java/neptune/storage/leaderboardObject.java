package neptune.storage;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
public class leaderboardObject {
    @JsonProperty("leaderboards")
    private Map <String, Integer> leaderboards;

    public leaderboardObject(){
        leaderboards = new HashMap<>();
    }
    @JsonIgnore
    public int getPoints(String MemberID){
        return leaderboards.getOrDefault(MemberID,0);
    }
    @JsonIgnore
    public void incrimentPoint(String MemberID){
        int points = leaderboards.getOrDefault(MemberID, 0);
        points ++;
        if (leaderboards.containsKey(MemberID)){
            leaderboards.replace(MemberID, points);
        }
        else{
            leaderboards.put(MemberID,points);
        }
    }
    @JsonIgnore
    public LinkedHashMap<String, Integer> getTopUsers(){
        //https://howtodoinjava.com/sort/java-sort-map-by-values/
        LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
        //Use Comparator.reverseOrder() for reverse ordering
        leaderboards.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
        int resultSize = reverseSortedMap.size();
        if (resultSize > 10){
            resultSize = 10;
        }
        LinkedHashMap<String, Integer> finalSortedMap = new LinkedHashMap<>();
        Iterator<Map.Entry<String, Integer>> entry = reverseSortedMap.entrySet().iterator();

        for (int i = 0; i <=resultSize; i++){
            Map.Entry<String,Integer> entry1 = entry.next();
            finalSortedMap.put(entry1.getKey(),entry1.getValue());
        }
        return finalSortedMap;
    }
}