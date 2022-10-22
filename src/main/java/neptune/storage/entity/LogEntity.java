package neptune.storage.entity;

import jakarta.persistence.*;
import org.hibernate.Session;

import java.sql.Timestamp;

@Entity
@Table(name= "GUILD_LOGGING")
@Cacheable
public class LogEntity {
    public LogEntity() {
        timestamp = new Timestamp(System.currentTimeMillis());
    }

    public String getMemberID() {
        return memberID;
    }

    public void setMemberID(String memberID) {
        this.memberID = memberID;
    }

    public String getChannelID() {
        return channelID;
    }

    public void setChannelID(String channelID) {
        this.channelID = channelID;
    }

    public String getGuildID() {
        return guildID;
    }

    public void setGuildID(String guildID) {
        this.guildID = guildID;
    }

    public String getMessageID() {
        return messageID;
    }

    public void setMessageID(String messageID) {
        this.messageID = messageID;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }

    public int getVersion() {
        return version;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    @Column(name = "MEMBER_ID")
    private String memberID;
    @Column(name = "CHANNEL_ID")
    private String channelID;
    @Column(name = "GUILD_ID")
    private String guildID;
    @Column(name = "MESSAGE_ID")
    @Id
    private String messageID;

    @Column(length = 4000, name = "MESSAGE_CONTENT")
    private String messageContent;
    @Version
    private int version;
    // timestamp exists for storage cleanup.

    @Column(name = "TIMESTAMP")
    @Temporal(TemporalType.TIMESTAMP)
    private final Timestamp timestamp;

    @ManyToOne
    @JoinColumn(name = "guildID", insertable = false, updatable = false)
    private GuildEntity guildEntity;

    public void setVersion(int version) {
        this.version = version;
    }

    public GuildEntity getGuildEntity() {
        return guildEntity;
    }

    public void setGuildEntity(GuildEntity guildEntity) {
        this.guildEntity = guildEntity;
    }
}
