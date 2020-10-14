package neptune.storage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSetter;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class guildObject {
  @JsonGetter("logOptions")
  public logOptionsObject getLogOptions() {
    return logOptions;
  }

  @JsonGetter("customRole")
  public customRoleObject getCustomRole() {
    return customRole;
  }

  @JsonGetter("leaderboard")
  public leaderboardObject getLeaderboard() {
    return leaderboard;
  }

  @JsonSetter("leaderboard")
  public void setLeaderboard(leaderboardObject leaderboardEntity) {
    leaderboard = leaderboardEntity;
  }

  @JsonGetter("guildOptions")
  public guildOptionsObject getGuildOptions() {
    return guildOptions;
  }

  @JsonSetter("logOptions")
  public void setLogOptionsEntity(logOptionsObject logOptionsEntity) {
    logOptions = logOptionsEntity;
  }

  @JsonProperty("logOptions")
  private logOptionsObject logOptions;

  @JsonProperty("customRole")
  private customRoleObject customRole;

  @JsonProperty("leaderboard")
  private leaderboardObject leaderboard;

  @JsonProperty("guildOptions")
  private guildOptionsObject guildOptions;

  private guildObject() {}
  ;

  public guildObject(String GuildID) {
    guildID = GuildID;
    version = 1;
    logOptions = new logOptionsObject();
    customRole = new customRoleObject();
    leaderboard = new leaderboardObject();
    guildOptions = new guildOptionsObject();
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
