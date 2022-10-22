package neptune.storage.entity;

import jakarta.persistence.*;
import neptune.storage.entity.GuildEntity;

import java.util.Objects;

@Entity
@Table(name= "GUILD_LOGGING_OPTIONS")
@Cacheable
public class LogOptionsEntity {

    @Column(nullable = false)
    private String Channel = "";
    private boolean TextChannelLogging = false;
    private boolean VoiceChannelLogging = false;
    private boolean ServerModificationLogging = false;
    private boolean MemberActivityLogging = false;
    private boolean GlobalLogging = false;

    public LogOptionsEntity() {
    }

    public boolean isTextChannelLogging() {
        return TextChannelLogging;
    }

    public void setTextChannelLogging(boolean textChannelLogging) {
        TextChannelLogging = textChannelLogging;
    }

    public boolean isVoiceChannelLogging() {
        return VoiceChannelLogging;
    }

    public void setVoiceChannelLogging(boolean voiceChannelLogging) {
        VoiceChannelLogging = voiceChannelLogging;
    }

    public boolean isServerModificationLogging() {
        return ServerModificationLogging;
    }

    public void setServerModificationLogging(boolean serverModificationLogging) {
        ServerModificationLogging = serverModificationLogging;
    }

    public boolean isMemberActivityLogging() {
        return MemberActivityLogging;
    }

    public void setMemberActivityLogging(boolean memberActivityLogging) {
        MemberActivityLogging = memberActivityLogging;
    }

    public boolean isGlobalLogging() {
        return GlobalLogging;
    }

    public void setGlobalLogging(boolean globalLogging) {
        GlobalLogging = globalLogging;
    }

    public GuildEntity getGuildEntity() {
        return guildEntity;
    }

    public void setGuildEntity(GuildEntity guildEntity) {
        this.guildEntity = guildEntity;
    }

    public String getChannel() {
        return Channel;
    }

    public void setChannel(String channelID) {
        Channel = Objects.requireNonNullElse(channelID, "");
    }
    @Id
    @OneToOne
    @JoinColumn(name = "GuildID")
    private GuildEntity guildEntity;
}
