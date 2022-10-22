package neptune.storage.entity;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

//Stop using Subclasses
@Entity
@Table(name= "GUILD")
@Cacheable
public class GuildEntity {
    public GuildEntity() {

    }
    @OneToMany(mappedBy = "guildEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProfileEntity> profile;

    @OneToOne(mappedBy = "guildEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private GuildOptionsEntity config;

    @OneToMany(mappedBy = "guildEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LogEntity> loggedMessages;

    @OneToOne(mappedBy = "guildEntity", cascade = CascadeType.ALL, orphanRemoval = true)
    private LogOptionsEntity logConfig;

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    @Id
    private String guildID;


    @Version
    private int version;

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public List<ProfileEntity> getProfile() {
        if (profile == null) {
            profile = new ArrayList<>();
        }
        return profile;
    }

    public void setProfile(List<ProfileEntity> profile) {
        this.profile = profile;
    }

    public GuildOptionsEntity getConfig() {

        if (config == null) {
            config = new GuildOptionsEntity();
        }
        return config;
    }

    public void setConfig(GuildOptionsEntity config) {
        this.config = config;
    }

    public List<LogEntity> getLoggedMessages() {
        if (loggedMessages == null) {
            loggedMessages = new ArrayList<>();
        }
        return loggedMessages;
    }

    public void setLoggedMessages(List<LogEntity> loggedMessages) {
        this.loggedMessages = loggedMessages;
    }

    public LogOptionsEntity getLogConfig() {

        if (logConfig == null) {
            logConfig = new LogOptionsEntity();
        }
        return logConfig;
    }

    public void setLogConfig(LogOptionsEntity logConfig) {
        this.logConfig = logConfig;
    }
}
