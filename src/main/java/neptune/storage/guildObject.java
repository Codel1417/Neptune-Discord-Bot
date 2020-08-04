package neptune.storage;

import com.fasterxml.jackson.annotation.JsonProperty;

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
    @JsonProperty
    private logOptionsObject logOptionsEntity;
    @JsonProperty
    private customRoleObject customRoleEntity;
    @JsonProperty
    private leaderboardObject leaderboardEntity;
    @JsonProperty
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
    @JsonProperty
    private String guildID;
    @JsonProperty
    int version;

    
}
