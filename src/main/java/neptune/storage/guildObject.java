package neptune.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonAutoDetect(fieldVisibility=JsonAutoDetect.Visibility.ANY)
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
    private guildObject(){};
    public guildObject(String GuildID){
        guildID = GuildID;
        version = 1;
        logOptionsEntity = new logOptionsObject();
        customRoleEntity = new customRoleObject();
        leaderboardEntity = new leaderboardObject();
        guildOptionsEntity = new guildOptionsObject();
    }
    @JsonGetter("guildID")
    public String getGuildID() {
        return guildID;
    }
    @JsonSetter("guildID")
    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }
    @JsonGetter("version")
    public int getVersion() {
        return version;
    }
    @JsonSetter("version")
    public void setVersion(int version) {
        this.version = version;
    }
    @JsonProperty("guildID")
    private String guildID;
    @JsonProperty("version")
    int version;

    
}
