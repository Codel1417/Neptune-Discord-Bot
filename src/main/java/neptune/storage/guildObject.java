package neptune.storage;

import java.util.*;

public class guildObject {
    public logOptionsObject getLogOptions() {
        return logOptionsEntity;
    }

    public customRoleObject getCustomRole() {
        return customRoleEntity;
    }

    public leaderboardObject getLeaderboard() {
        return leaderboardEntity;
    }

    public guildOptionsObject getGuildOptions() {
        return guildOptionsEntity;
    }

    private logOptionsObject logOptionsEntity;
    private customRoleObject customRoleEntity;
    private leaderboardObject leaderboardEntity;
    private guildOptionsObject guildOptionsEntity;

    public guildObject(String GuildID){
        guildID = GuildID;
        version = 1;
        logOptionsEntity = new logOptionsObject();
        customRoleEntity = new customRoleObject();
        leaderboardEntity = new leaderboardObject();
        guildOptionsEntity = new guildOptionsObject();
    }
    public String getGuildID() {
        return guildID;
    }
    public int getVersion() {
        return version;
    }

    private String guildID;
    int version;

    public class logOptionsObject{
        private Map<String, Boolean> loggingOptions;
        private String Channel;
        public logOptionsObject(){
            loggingOptions = new HashMap<>();
            Channel = null;
        }
        public boolean getOption(String option){
            return loggingOptions.getOrDefault(option,false);
        }
        public void setOption(String option, Boolean value){
            loggingOptions.put(option,value);
        }
        public String getChannel(){
            return Channel;
        }
        public void setChannel(String channelID){
            Channel = channelID;
        }
    }
    public class customRoleObject{
        private Map<String, String> customRoles;
        public customRoleObject(){
            customRoles = new HashMap<>();
        }
        public String getRoleID(String MemberID){
            return customRoles.getOrDefault(MemberID,null);
        }
        public void addRole(String MemberID, String RoleID){
            customRoles.put(MemberID,RoleID);
        }
    }
    public class leaderboardObject{
        private Map <String, Integer> leaderboards;
        public leaderboardObject(){
            leaderboards = new HashMap<>();
        }
        public int getPoints(String MemberID){
            return leaderboards.getOrDefault(MemberID,0);
        }
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
    public class guildOptionsObject{
        private Map<options, Boolean> GuildOptions;
        public guildOptionsObject(){
            GuildOptions = new HashMap<>();
        }
        public boolean getOption(options Option){
            return GuildOptions.getOrDefault(Option,false);
        }
        public void setOption(options Option, Boolean value){
            GuildOptions.put(Option,value);
        }
    }
}
