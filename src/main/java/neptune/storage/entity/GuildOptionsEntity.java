package neptune.storage.entity;

import jakarta.persistence.*;
import neptune.storage.entity.GuildEntity;

@Entity
@Table(name= "GUILD_OPTIONS")
@Cacheable
public class GuildOptionsEntity {
    @Id
    @OneToOne
    @JoinColumn(name = "GuildID")
    private GuildEntity guildEntity;
    private boolean CustomRoleEnabled = false;
    private boolean LeaderboardEnabled = true;
    private boolean CustomSoundsEnabled = false;
    private boolean LeaderboardLevelUpNotification = false;
    private boolean LoggingEnabled = false;

    public GuildOptionsEntity() {
    }

    public GuildEntity getGuildEntity() {
        return guildEntity;
    }

    public void setGuildEntity(GuildEntity guildEntity) {
        this.guildEntity = guildEntity;
    }

    public boolean isCustomRoleEnabled() {
        return CustomRoleEnabled;
    }

    public void setCustomRoleEnabled(boolean customRoleEnabled) {
        CustomRoleEnabled = customRoleEnabled;
    }

    public boolean isLeaderboardEnabled() {
        return LeaderboardEnabled;
    }

    public void setLeaderboardEnabled(boolean leaderboardEnabled) {
        LeaderboardEnabled = leaderboardEnabled;
    }

    public boolean isCustomSoundsEnabled() {
        return CustomSoundsEnabled;
    }

    public void setCustomSoundsEnabled(boolean customSoundsEnabled) {
        CustomSoundsEnabled = customSoundsEnabled;
    }

    public boolean isLeaderboardLevelUpNotification() {
        return LeaderboardLevelUpNotification;
    }

    public void setLeaderboardLevelUpNotification(boolean leaderboardLevelUpNotification) {
        LeaderboardLevelUpNotification = leaderboardLevelUpNotification;
    }

    public boolean isLoggingEnabled() {
        return LoggingEnabled;
    }

    public void setLoggingEnabled(boolean loggingEnabled) {
        LoggingEnabled = loggingEnabled;
    }
}
