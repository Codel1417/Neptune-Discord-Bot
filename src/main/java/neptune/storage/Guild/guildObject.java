package neptune.storage.Guild;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

import neptune.storage.Enum.GuildOptionsEnum;
import neptune.storage.Enum.LoggingOptionsEnum;

@JsonSerialize(using = guildObjectSerializer.class)
@JsonDeserialize(using = guildObjectDeserializer.class)

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.NONE)
public class guildObject {
    public logOptionsObject getLogOptions() {
        return logOptions;
    }

    public customRoleObject getCustomRole() {
        return customRole;
    }

    public leaderboardObject getLeaderboard() {
        return leaderboard;
    }

    public void setLeaderboard(leaderboardObject leaderboardEntity) {
        leaderboard = leaderboardEntity;
    }

    public guildOptionsObject getGuildOptions() {
        return guildOptions;
    }

    public void setLogOptionsEntity(logOptionsObject logOptionsEntity) {
        logOptions = logOptionsEntity;
    }

    private logOptionsObject logOptions;

    private customRoleObject customRole;

    private leaderboardObject leaderboard;

    private guildOptionsObject guildOptions;


    public guildObject(String GuildID) {
        guildID = GuildID;
        version = 2;
        logOptions = new logOptionsObject();
        customRole = new customRoleObject();
        leaderboard = new leaderboardObject();
        guildOptions = new guildOptionsObject();
    }
    public guildObject(String GuildID, Map<GuildOptionsEnum,Boolean> guildOptionsMap, Map<LoggingOptionsEnum,Boolean> logOptionsMap, Map<String,Integer> leaderboardMap, Map<String,String> customRoleMap, String loggingChannel, int version) {
        guildID = GuildID;
        version = this.version;
        logOptions = new logOptionsObject(loggingChannel,logOptionsMap);
        customRole = new customRoleObject(customRoleMap);
        leaderboard = new leaderboardObject(leaderboardMap);
        guildOptions = new guildOptionsObject(guildOptionsMap);
    }
    public String getGuildID() {return guildID;}

    protected void setGuildID(String guildID) {this.guildID = guildID;}

    public int getVersion() {return version;}

    protected void setVersion(int version) {this.version = version;}

    private String guildID;
    int version;


    public class guildOptionsObject {
        private Map<GuildOptionsEnum, Boolean> GuildOptionsHashMap;
    
        public guildOptionsObject() {
            GuildOptionsHashMap = new HashMap<>();
        }
        protected guildOptionsObject(Map<GuildOptionsEnum, Boolean> GuildOptionsMap){
            GuildOptionsHashMap =  GuildOptionsMap;
        }

        public boolean getOption(GuildOptionsEnum Option) {
            return GuildOptionsHashMap.getOrDefault(Option, false);
        }
    
        public void setOption(GuildOptionsEnum Option, Boolean value) {
            GuildOptionsHashMap.put(Option, value);
        }
        protected Map<GuildOptionsEnum, Boolean> getGuildOptions(){
            return GuildOptionsHashMap;
        };
    }
    
    public class logOptionsObject {
        private Map<LoggingOptionsEnum, Boolean> loggingOptions;
    
        private String Channel = null;
    
        public logOptionsObject() {
            loggingOptions = new HashMap<>();
            Channel = null;
        }
        protected logOptionsObject(String channel, Map<LoggingOptionsEnum, Boolean> loggingOptionsMap){
            loggingOptions = loggingOptionsMap;
            Channel = channel;
        }
        
        protected Map<LoggingOptionsEnum, Boolean> getloggingOptionsMap(){
            return loggingOptions;
        }
        public boolean getOption(LoggingOptionsEnum option) {
            return loggingOptions.getOrDefault(option, false);
        }
    
        public void setOption(LoggingOptionsEnum option, Boolean value) {
            loggingOptions.put(option, value);
        }
    
        public String getChannel() {
            return Channel;
        }
    
        public void setChannel(String channelID) {
            Channel = channelID;
        }
    }
    
    public class leaderboardObject {
        private Map<String, Integer> leaderboards = new HashMap<>();

        public leaderboardObject(Map<String, Integer> leaderboardsMap) {
            leaderboards = leaderboardsMap;
        }

        protected leaderboardObject(){
            leaderboards = new HashMap<>();
        }

        public int getPoints(String MemberID) {
            return leaderboards.getOrDefault(MemberID, 0);
        }

        public void incrimentPoint(String MemberID) {
            int points = leaderboards.getOrDefault(MemberID, 0);
            points++;
            if (leaderboards.containsKey(MemberID)) {
                leaderboards.replace(MemberID, points);
            } else {
                leaderboards.put(MemberID, points);
            }
        }

        public LinkedHashMap<String, Integer> getTopUsers() {
            // https://howtodoinjava.com/sort/java-sort-map-by-values/
            LinkedHashMap<String, Integer> reverseSortedMap = new LinkedHashMap<>();
            // Use Comparator.reverseOrder() for reverse ordering
            leaderboards.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .forEachOrdered(x -> reverseSortedMap.put(x.getKey(), x.getValue()));
            int resultSize = reverseSortedMap.size();
            if (resultSize > 10) {
                resultSize = 10;
            }
            LinkedHashMap<String, Integer> finalSortedMap = new LinkedHashMap<>();
            Iterator<Map.Entry<String, Integer>> entry = reverseSortedMap.entrySet().iterator();

            for (int i = 0; i <= resultSize; i++) {
                if (entry.hasNext()){
                    Map.Entry<String, Integer> entry1 = entry.next();
                    finalSortedMap.put(entry1.getKey(), entry1.getValue());
                }
                else {
                    break;
                }

            }
            return finalSortedMap;
        }

        protected Map<String, Integer> getLeaderboards() {
            return leaderboards;
        }
    }
    public class customRoleObject {
        private Map<String, String> customRoles;
    
        public customRoleObject() {
            customRoles = new HashMap<>();
        }
        protected customRoleObject(Map<String, String> customRolesMap) {
            customRoles = customRolesMap;
        }
        public String getRoleID(String MemberID) {
            return customRoles.getOrDefault(MemberID, null);
        }
    
        public void addRole(String MemberID, String RoleID) {
            customRoles.put(MemberID, RoleID);
        }
    
        public void removeRole(String MemberID) {
            customRoles.remove(MemberID);
        }
        protected Map<String, String> getCustomRoles(){
            return customRoles;
        }
    }
}
